// Time-stamp: <08 déc 2009 08:30 queinnec@enseeiht.fr>

import java.util.concurrent.Semaphore;

public class PhiloSem implements StrategiePhilo {

    /****************************************************************/
	static Semaphore[] onPhilosopherIsHungry;
	static EtatPhilosophe[] philosopherStates;
	static Semaphore mutex = new Semaphore(1);
	
    public PhiloSem (int nbPhilosophes) {
    	onPhilosopherIsHungry = new Semaphore[nbPhilosophes];
    	philosopherStates = new EtatPhilosophe[nbPhilosophes];
    	
    	for (int i = 0; i < nbPhilosophes; i++) {
    		onPhilosopherIsHungry[i] = new Semaphore(0);
    		philosopherStates[i] = EtatPhilosophe.Pense;
		}
    }
    
    /** Le philosophe no demande les fourchettes.
     *  Pr�condition : il n'en poss�de aucune.
     *  Postcondition : quand cette m�thode retourne, il poss�de les deux fourchettes adjacentes � son assiette. */
    public void demanderFourchettes (int no) throws InterruptedException
    {

    	mutex.acquire();
    	if(peutManger(no)) {
    		philosopherStates[no] = EtatPhilosophe.Mange;
        	mutex.release();   
    	}
    	else {
	    	philosopherStates[no] = EtatPhilosophe.Demande;
	    	mutex.release();  
	    	onPhilosopherIsHungry[no].acquire();
    	}
    	
    	
    }

    /** Le philosophe no rend les fourchettes.
     *  Pr�condition : il poss�de les deux fourchettes adjacentes � son assiette.
     *  Postcondition : il n'en poss�de aucune. Les fourchettes peuvent être libres ou r�attribu�es � un autre philosophe. */
    public void libererFourchettes (int no)
    {
    	try {
			mutex.acquire();
		   	philosopherStates[no] = EtatPhilosophe.Pense;

	    	
	    	if(philosopherStates[Main.PhiloDroite(no)] == EtatPhilosophe.Demande && peutManger(Main.PhiloDroite(no))) {
	    		philosopherStates[Main.PhiloDroite(no)] = EtatPhilosophe.Mange;
	    		onPhilosopherIsHungry[Main.PhiloDroite(no)].release();
	    	}
	    	
	    	if(philosopherStates[Main.PhiloGauche(no)] == EtatPhilosophe.Demande && peutManger(Main.PhiloGauche(no))) {
	    		philosopherStates[Main.PhiloGauche(no)] = EtatPhilosophe.Mange;
	    		onPhilosopherIsHungry[Main.PhiloGauche(no)].release();
	    	}
    		
	    	mutex.release();
    	} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public boolean peutManger(int no) {
    	return (philosopherStates[Main.PhiloDroite(no)] != EtatPhilosophe.Mange && philosopherStates[Main.PhiloGauche(no)] != EtatPhilosophe.Mange);
    }
    
    /** Nom de cette stratégie (pour la fenêtre d'affichage). */
    public String nom() {
    	return "Implantation S�maphores, strat�gie \"Etats des Philosophes\"";
    }
}

