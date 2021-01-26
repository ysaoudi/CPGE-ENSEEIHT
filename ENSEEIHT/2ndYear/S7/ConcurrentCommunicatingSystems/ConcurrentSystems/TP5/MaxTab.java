package max;

//v0.1.1 15/11/20 (PM) (0.1.1 :clarification du message d'erreur principal)

import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

import java.util.List;
import java.util.LinkedList;

import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

class MaxLocal  implements Callable<Integer> {
// pool fixe
    private int debut;
    private int fin;
    private int[] tableau;

    MaxLocal(int[] t, int d, int f) {
        debut = d;
        fin = f;
        tableau = t;
    }

    @Override
    public	Integer call() { // le resultat doit etre un objet
        int ml = 0;
        for (int i = debut; i < fin; i++) {
            ml = Math.max(tableau[i], ml);
        }
        return ml;
    }
}

class TraiterFragment extends RecursiveTask<Integer> {
// pool fork-join
    private int debut;
    private int fin;
    private int[] tableau;
    static int seuil;

    private int max = 0;

    TraiterFragment(int[] t, int d, int f) {
        debut = d;
        fin = f;
        tableau = t;
    }

    protected Integer compute() {
        int taille = fin - debut +1;
        // si la tache est trop grosse, on la decompose en 2
        if (taille > seuil) {
        	TraiterFragment sp1 = new TraiterFragment(tableau, debut, (int)(debut + fin)/2); 
        	TraiterFragment sp2 = new TraiterFragment(tableau, 1 + (int)(debut + fin)/2, fin); 
        	
        	sp1.fork();
        	sp2.fork();
        	max = Math.max(sp1.join(), sp2.join());        	
        } else {
            for (int i = 0; i <tableau.length; i++) {
                max = Math.max(tableau[i], max);
            }
        }
            return max;
    }
}

public class MaxTab {
//--------- Mono
    static int maxMono(int[] t) {
        int max = 0;
        for (int i = 0; i <t.length; i++) {
            max = Math.max(t[i], max);
        }
        return max;
    }

//-------- Pool Fixe
    static int maxPoolFixe(ExecutorService xs, int[] t, int nbT)
    throws InterruptedException, ExecutionException {
        int max = 0;
        int d = 0;									//indice de depart d'une recherche locale
        int f = 0;  						//indice de fin d'une recherche locale
        int grain = Math.max(1,t.length/nbT); 	/*taille d'une recherche locale 
        										 * = taille du tableau/nombre de taches lancees
        										 * (ou 1 dans le cas (aberrant) ou il y a plus
        										 * de taches que d'elements du tableau) */
        List<Future<Integer>> resultats=new LinkedList<Future<Integer>>();

        //soumettre les taches
        for (int i = 0; i < nbT; i++) {
        	d = i*grain;
        	f = (i == nbT) ? t.length - 1 : d + grain - 1;
        	
        	resultats.add(xs.submit(new MaxLocal(t, d, f)));
        }

        //recuperer les resultats
        for (Future<Integer> result : resultats) {
        	int maxLocal = result.get();
        	max = Math.max(max, maxLocal);
        }
        return max;
    }

//-------- Pool ForkJoin
    static int maxForkJoin(ForkJoinPool f, int[] t) {
        TraiterFragment tout = new TraiterFragment(t, 0, t.length-1);
        int max = f.invoke(tout);
        return max;
    }

//-------- main
    public static void main(String[] args)
    throws InterruptedException, ExecutionException, IOException, FileNotFoundException {

        
    	 
        int nbOuvriersPool=0; // nb ouvriers du pool fixe. Bonne valeur : nb de processeurs disponibles
        int nbEssais=-1;
        int nbTaches=-1;
        int tailleTroncon=-1; // nb d'elements du tableau traites par chaque ouvrier (FJ) 
        String chemin="";
        int[] tableau;
        long depart, fin;
        int max;

        long[] tempsMono, tempsPool,tempsForkJoin;

        if (args.length == 5) { //analyse des parametres
            chemin = args[0];
            try {
                nbEssais = Integer.parseInt (args[1]);
                nbTaches = Integer.parseInt (args[2]);
                tailleTroncon = Integer.parseInt (args[3]);
                nbOuvriersPool = Integer.parseInt (args[4]);
            }
            catch (NumberFormatException nfx) {
                throw new IllegalArgumentException("\nUsage : MaxTab <fichier> <nb essais>"
                                          	+" <nb taches (pool)> <taille troncon (FJ)>"
                                            + " <nb ouvriers du pool (pool)>\n"
                                            + " * <nb taches (pool)> = nb de fragments a traiter \n"
                                            + " * <taille troncon (FJ)> = taille du fragment \n");
                                    					}
        }
        if ((nbEssais<1) || (nbTaches<1) || (tailleTroncon<1) || (nbOuvriersPool<1)
                || !Files.isRegularFile(Paths.get(chemin), LinkOption.NOFOLLOW_LINKS))
                throw new IllegalArgumentException("\nUsage : MaxTab <fichier> <nb essais>"
                                          	+" <nb taches (pool)> <taille troncon (FJ)>"
                                            + " <nb ouvriers du pool (pool)>\n"
                                            + " * <nb taches (pool)> = nb de fragments a traiter \n"
                                            + " * <taille troncon (FJ)> = taille du fragment \n"); 
        //appel correct
        tempsMono = new long[nbEssais];
        tempsPool = new long[nbEssais];
        tempsForkJoin = new long[nbEssais];
        tableau=TableauxDisque.charger(chemin);
        System.out.println(Runtime.getRuntime().availableProcessors()+" processeurs disponibles pour la JVM");

        //creer un pool avec un nombre fixe d'ouvriers
        ExecutorService poule = Executors.newFixedThreadPool(nbOuvriersPool);

        //creer un pool ForkJoin
        ForkJoinPool fjp = new ForkJoinPool();
        TraiterFragment.seuil=tailleTroncon;
        
        System.out.println("Execution avec : "
        		+ "\n nbEssais = " + nbEssais
        		+ "\n nbTaches = " + nbTaches 
        		+ "\n tailleTroncon = " + tailleTroncon
        		+ "\n nbOuvriersPool = " + nbOuvriersPool);
        System.out.println("--------------------");
        System.out.println("--------------------");
        
        for (int i = 0; i < nbEssais; i++) {
            depart = System.nanoTime();
            max = maxMono(tableau);
            fin = System.nanoTime();
            tempsMono[i] = (fin - depart);
            System.out.println("Essai ["+i+"] : maximum = " + max+", duree (mono) " +
                               tempsMono[i]/1_000+ "µs");
        }
        System.out.println("--------------------");

        for (int i = 0; i < nbEssais; i++) {
            depart = System.nanoTime();
            max = maxPoolFixe(poule, tableau, nbTaches);
            fin = System.nanoTime();
            tempsPool[i] = (fin - depart);
            System.out.println("Essai ["+i+"] : maximum = " + max+", duree (pool) " +
                               tempsPool[i]/1_000+ "µs");
        }
        poule.shutdown();
        System.out.println("--------------------");

        for (int i = 0; i < nbEssais; i++) {
            depart = System.nanoTime();
            max = maxForkJoin(fjp,tableau);
            fin = System.nanoTime();
            tempsForkJoin[i] = (fin - depart);
            System.out.println("Essai ["+i+"] : maximum = " + max+", duree (FJ) " +
                               tempsForkJoin[i]/1_000+ "µs");
        }
        System.out.println("--------------------");
    }
}