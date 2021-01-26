// V0.1.1 - PM 22/09/20
import java.util.concurrent.atomic.AtomicLong;

interface Incrementeur extends Runnable{
    void incr();
    /* Boucle d'incrementation effectuee par un processus (thread).
      Le principe/objectif est de realiser cette boucle, comportant un meme nombre
      d'incrementations en suivant differents schemas pour maintenir la coherence
      (ou pas) et de comparer ces schemas en termes de correction et d'efficacite
     */
}

public class IncrMesAtomicLong {

    static AtomicLong cpt = new AtomicLong(0L);
    static final long NB_IT = 1000L;			 //Nb d'iterations de la boucle externe 10^3
    static final long NB_IT_INTERNES = 1000000L; //Nb d'iterations de la boucle interne 10^6
    static long Attente_ms = 10L;
    static final int Attente_nano = 0;
    static Thread[] activites;          // Tableau des processus
    static Object mutex = new Object(); // pour les blocs synchronized

    static void lancer(int nbA, Incrementeur r) {
        // Initialisation du/des compteur(s) incrementes
        cpt = new AtomicLong(0L);
        /* Creation et lancement des threads.
          Chaque thread execute le meme code, qui est la methode run() d'une classe
          implantant un Incrementeur (autrement dit un schema de realisation particulier
          de la boucle dincrementation)
         */

        for (int i = 0; i < nbA ; i++) {
            try {
                IncrMesAtomicLong.activites[i] = new Thread(r,"t"+i);
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
            IncrMesAtomicLong.activites[i].start(); // permet de lancer l'instance de thread auquel elle est appliquee
        }
    }

    static void finir() {
        // Attente de la terminaison des differents threads lances
        for (int i = 0; i < IncrMesAtomicLong.activites.length ; i++) {
            try {
                IncrMesAtomicLong.activites[i].join(); // permet d'attendre la terminaison de l'instance de thread auquel elle est appliquee
            }
            catch (InterruptedException e)
            {
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) {
        int nbActivites = 3;
        int nbMesures = 10;
        int i = 0;
        long depart, fin;
        boolean mutex = true;

        // Chargement des parametres saisis en ligne de commande
        if (args.length == 3) {
            try {
                nbActivites = Integer.parseInt (args[0]);
                Attente_ms = Integer.parseInt (args[1]);
                nbMesures = Integer.parseInt (args[2]);
            }
            catch (NumberFormatException nfx) {
                Attente_ms = 0;
            }
            if ((nbActivites < 1) || (Attente_ms < 1) || (nbMesures < 1)) {
                System.out.println("Usage : IncrDemoSeq <Nb activites> <duree pause (ms)> <nbMesures>,"
                                   +"avec Nb activites, duree pause, nbMesures >= 1");
                System.exit (1);
            }
        } else {
            System.out.println("Nb d'arguments ≠ 3. Execution avec 10 mesures, 3 activites et 10 ms de pause");
        }

        activites = new Thread[nbActivites];

        for (int it=1; it<=nbMesures; it++) { // 
        // Mesure iteree, pour eviter des ecarts trop importants sur les premieres mesures
        // (du fait de possibles optimisations (processeur, cache, compilateur).
        // Idee = retenir une moyenne des dernieres mesures.
            // Boucle sequentielle : NB_IT x NB_IT_INTERNES x nbActivites iterations
            
 		    depart = System.nanoTime();
            
             for (long li = 0; li < nbActivites*IncrMesAtomicLong.NB_IT; li++) {
                for (long j = 1; j<=IncrMesAtomicLong.NB_IT_INTERNES; j++) {
                    IncrMesAtomicLong.cpt=new AtomicLong(IncrMesAtomicLong.cpt.incrementAndGet());
                    // j/j pour tenter de dejouer les optimisations du compilateur
                }
                try {
                    Thread.sleep(IncrMesAtomicLong.Attente_ms, IncrMesAtomicLong.Attente_nano);
                    // sleep pour modeliser un temps de calcul sans conflit d'acces et...
                    // pour tenter de dejouer les optimisations du cache
                }
                catch (InterruptedException ie)
                {
                    System.out.println("InterruptedException : " + ie);
                }
            }
            fin = System.nanoTime();
            System.out.println("Duree execution mono : " + (fin-depart)/1000000L);
            System.out.println();

           
            depart = System.nanoTime();
            // Lancement de nbActivites processus effectuant
            // chacun NB_IT x NB_IT_INTERNES iterations
            lancer(nbActivites, new IncrementeurNonSync());
            finir();
            fin = System.nanoTime();
            System.out.println("Duree execution non synchronisee (ms): " + (fin-depart)/1000000L);
            System.out.println(); System.out.println();
        }
        // Completer ici, en definissant et mesurant differents Incrementeur
        // de maniere analogue a ce qui est fait pour IncrementeurNonSync
    }
}

class IncrementeurNonSync implements Incrementeur {

    /* Exemple d'incrementeur :
     -la methode incr effectue les incrementations sans gerer les conflis d'acces au compteur
     - la methode run() appelle incr()
     */
    public void incr() {
        for (long i = 0L; i < IncrMesAtomicLong.NB_IT; i++) {
            // boucle imbriquee pour permettre (eventuellement) de tester differents
            // grains de synchronisation

            for (long j = 1; j<=IncrMesAtomicLong.NB_IT_INTERNES; j++) {
                
                IncrMesAtomicLong.cpt=new AtomicLong(IncrMesAtomicLong.cpt.incrementAndGet());
            }
            try {
                Thread.sleep(IncrMesAtomicLong.Attente_ms, IncrMesAtomicLong.Attente_nano);
            }
            // Attente depour eviter l'utilisation du cache et modeliser
            // une partie du calcul sans conflit.
            // Vous pouvez  (eventuellement) commenter l'attente
            // pour voir l'effet (interessant) du mecanisme de cache,
            // ou encore modifier  la valeur de l'attente pour voir
            // l'effet du grain de l'exclusion mutuelle sur les temps
            // d'execution
            catch (InterruptedException ie)
            {
                System.out.println("InterruptedException : " + ie);
            }
        }
    }

    public void run() {
        // afficher eventuellement la valeur du compteur avant/apres
        // pour verifier la coherence des incrementations
        System.out.println("Thread " + Thread.currentThread().getName() + " - compteur avant : " + IncrMesAtomicLong.cpt);
        this.incr();
        System.out.println("Thread " + Thread.currentThread().getName() + " - compteur après : " + IncrMesAtomicLong.cpt);
    }
}