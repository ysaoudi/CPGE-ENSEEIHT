//v0.2.1 15/11/20 (PM) (0.2.1 : + commentaires & clarification du message d'erreur principal)
package triFusion;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import java.util.concurrent.RecursiveTask;


import java.util.concurrent.ForkJoinPool;

class TriLocal  implements Callable<int[]> {
// pool fixe
    private int debut;
    private int fin;
    private int[] tableau;
    private int[] resultat;

    TriLocal(int[] t, int d, int f) {
        debut = d;
        fin = f;
        tableau = t;
    }

    @Override
    public	int[] call() {
        return TF.traiterTroncon(tableau, debut, fin) ;
    }
}

class TraiterFragment extends RecursiveTask<int[]> {
// pool fork-join
    private int debut;
    private int fin;
    private int[] tableau;
    int[] resTri=null;
    static int seuil;

    private int max = 0;

    TraiterFragment(int[] t, int d, int f) {
        debut = d;
        fin = f;
        tableau = t;
    }

    @Override
    protected int[] compute() {
        int taille;
        //si la tache est trop grosse, on la decompose en 2
        if ( fin - debut + 1 > TF.seuil) {
        	TraiterFragment sp1 = new TraiterFragment(tableau, debut, (int)(debut + fin)/2); 
        	TraiterFragment sp2 = new TraiterFragment(tableau, 1 + (int)(debut + fin)/2, fin); 
        	sp1.fork();
        	sp2.fork();
        	resTri = TF.fusion(sp1.compute(), sp2.join());
        }
        else {
        	resTri = TF.traiterTroncon(tableau, debut, fin);
        }
    	return resTri;
    }
}

public class TF {
    static int seuil;

    static int[] fusion(int[] t1, int[]t2) {
        // fusionne les tableaux tries t1 et t2 en un seul tableau trie (resultat)
    	if(t1 == null && t2 != null) {
    		return t2;
    	} else if (t1 != null && t2 == null) {
    		return t1;
    	}
        int [] resultat = new int[t1.length+t2.length];
        int i1=0;
        int i2=0;
        int iR=0;

        while ((iR<resultat.length) && (i1<t1.length) && (i2<t2.length)) {
            if (t1[i1]<t2[i2]) {
                resultat[iR]=t1[i1];
                i1++;
            } else {
                resultat[iR]=t2[i2];
                i2++;
            }
            iR++;
        }
        while (i1<t1.length) {
            resultat[iR]=t1[i1];
            i1++;
            iR++;
        }
        while (i2<t2.length) {
            resultat[iR]=t2[i2];
            i2++;
            iR++;
        }
        return resultat;
    }

    static int[] traiterTroncon(int[] t, int debut, int fin) {
        int taille;
        int[] resTri;
        //si le fragment est trop gros, on le decompose en 2
        if((fin-debut+1) > TF.seuil) {
            taille = (fin-debut+1)/2;
            return TF.fusion(traiterTroncon(t, debut, debut+taille-1),
            										traiterTroncon(t, debut+taille, fin));
        } else { //traitement direct : quicksort
            resTri = Arrays.copyOfRange(t, debut,fin+1);
            Arrays.sort(resTri);
            return resTri;
        }
    }

//--------- Mono
    static int[] TFMono(int[] t) {
        return traiterTroncon(t, 0, t.length-1);
    }

//-------- Pool Fixe
    static int[] TFPool(ExecutorService xs, int[] t, int nbT)
    throws InterruptedException, ExecutionException {
        int d; 									//indice de depart d'un tri local
        int f = 0; 							//indice de fin d'un tri local
        int grain = Math.max(1,t.length/nbT); 	/*taille d'une recherche locale
        										 * = taille du tableau/nombre de taches lancees
        										 * (ou 1 dans le cas (aberrant) ou il y a plus
        										 * de taches que d'elements du tableau) */
        
        List<Future<int[]>> resultats = new ArrayList<Future<int[]>>(nbT);
        List<int[]> resTri = new LinkedList<int[]>(); //recueille et fusionne les resultats

        //soumettre les taches
        for (int i = 0; i < nbT; i++) {
			d = i * grain;
			f = (i == nbT) ? t.length - 1 : d + grain - 1;

			resultats.add(xs.submit(new TriLocal(t, d, f)));
		}
        
        int[] res = resultats.get(0).get();
		for (int j = 1; j < nbT; j++) {
			res = TF.fusion(res, resultats.get(j).get());
		}
		return res;
    }
    
//-------- Pool ForkJoin
    static int[] TFForkJoin(ForkJoinPool f, int[] t) {
        TraiterFragment tout = new TraiterFragment(t,0, t.length-1);
        int[] resTri = f.invoke(tout);
        return resTri;
    }

//-------- main
    public static void main(String[] args)
    throws InterruptedException, ExecutionException, IOException, FileNotFoundException {

        int nbOuvriersPool=0; // nb ouvriers du pool fixe. Bonne valeur : nb de processeurs disponibles
        int nbEssais=0;
        int nbTaches=0;
        int tailleSeuil=0; //nombre d'elements du tableau en dessous duquel on effectue un tri directement
        String chemin="";
        int[] tableau;
        long depart, fin;
        int [] resTri;

        long[] tempsMono, tempsPool,tempsForkJoin;

        if (args.length == 5) { //analyse des parametres
            chemin = args[0];
            try {
                nbEssais = Integer.parseInt (args[1]);
                nbTaches = Integer.parseInt (args[2]);
                tailleSeuil = Integer.parseInt (args[3]);
                nbOuvriersPool = Integer.parseInt (args[4]);
            }
            catch (NumberFormatException nfx) {
                throw new IllegalArgumentException("\nUsage : TF <fichier> <nb essais> "
                                                   + " <nb taches (pool)> <seuil>"
                                                   + " <nb ouvriers du pool (pool)>\n"
                                                   + " * <nb taches (pool)> = nb de fragments a traiter \n"
                                                   + " * <seuil> = taille pour tri direct \n");
            }
        }

        if ((nbEssais<1) || (nbTaches<1) || (tailleSeuil<1) || (nbOuvriersPool<1)
                || !Files.isRegularFile(Paths.get(chemin), LinkOption.NOFOLLOW_LINKS))
            throw new IllegalArgumentException("\nUsage : TF <fichier> <nb essais> "
                                               + " <nb taches (pool)> <seuil>"
                                               + " <nb ouvriers du pool (pool)>\n"
                                               + " * <nb taches (pool)> = nb de fragments a traiter \n"
                                               + " * <seuil> = taille pour tri direct \n");
        // l'appel est correct
        tempsMono = new long[nbEssais];
        tempsPool = new long[nbEssais];
        tempsForkJoin = new long[nbEssais];
        tableau=TableauxDisque.charger(chemin);
        TF.seuil=tailleSeuil;

        System.out.println(Runtime.getRuntime().availableProcessors()+" processeurs disponibles pour la JVM");

        //creer un pool avec un nombre fixe d'ouvriers
        ExecutorService poule = Executors.newFixedThreadPool(nbOuvriersPool);

        //creer un pool ForkJoin
        ForkJoinPool fjp = new ForkJoinPool();
        TraiterFragment.seuil=tailleSeuil;
        
        for (int i = 0; i < nbEssais; i++) {
            depart = System.nanoTime();
            resTri = TFMono(tableau);
            fin = System.nanoTime();
            tempsMono[i] = (fin - depart);
            TableauxDisque.sauver(chemin+"trieMono",resTri);
            System.out.println("Essai ["+i+"] : duree (mono) " + tempsMono[i]/1_000+"µs");
        }
        System.out.println("--------------------");

        for (int i = 0; i < nbEssais; i++) {
            depart = System.nanoTime();
            resTri = TFPool(poule, tableau, nbTaches);
            fin = System.nanoTime();
            tempsPool[i] = (fin - depart);
            TableauxDisque.sauver(chemin+"triePF",resTri);
            System.out.println("Essai ["+i+"] : duree (PF) " + tempsPool[i]/1_000+"µs");
        }
        poule.shutdown();
        System.out.println("--------------------");

        for (int i = 0; i < nbEssais; i++) {
            depart = System.nanoTime();
            resTri = TFForkJoin(fjp,tableau);
            fin = System.nanoTime();
            tempsForkJoin[i] = (fin - depart);
            TableauxDisque.sauver(chemin+"trieFJ",resTri);
            System.out.println("Essai ["+i+"] : duree (FJ) "+tempsForkJoin[i]/1_000+"µs");
        }
        System.out.println("--------------------");
    }
}