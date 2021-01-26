// Time-stamp: <08 dÃ©c 2009 08:30 queinnec@enseeiht.fr>

import java.util.concurrent.Semaphore;
	
public class PhiloSemBaseSolution2 implements StrategiePhilo {


	/****************************************************************/
	static Semaphore[] availableForks;
	static Semaphore philosophersEating;

	public PhiloSemBaseSolution2(int nbPhilosophes) {
		availableForks = new Semaphore[nbPhilosophes];
		philosophersEating = new Semaphore(nbPhilosophes - 1);
		for (int i = 0; i < nbPhilosophes; i++) {
			availableForks[i] = new Semaphore(1);
		}
	}

	/**
	 * Le philosophe no demande les fourchettes. Précondition : il n'en possède
	 * aucune. Postcondition : quand cette méthode retourne, il possède les deux
	 * fourchettes adjacentes à son assiette.
	 */
	public void demanderFourchettes(int no) throws InterruptedException {
		philosophersEating.acquire();
		availableForks[Main.FourchetteDroite(no)].acquire();
		availableForks[Main.FourchetteGauche(no)].acquire();
	}

	/**
	 * Le philosophe no rend les fourchettes. Précondition : il possède les deux
	 * fourchettes adjacentes à son assiette. Postcondition : il n'en possède
	 * aucune. Les fourchettes peuvent Ãªtre libres ou réattribuées à un autre
	 * philosophe.
	 */
	public void libererFourchettes(int no) {
		availableForks[Main.FourchetteDroite(no)].release();
		availableForks[Main.FourchetteGauche(no)].release();
		philosophersEating.release();
	}

	/** Nom de cette stratÃ©gie (pour la fenÃªtre d'affichage). */
	public String nom() {
		return "Implantation Sémaphores, stratégie \"Fourchettes en tant que ressources critiques: Solution 2 de l'interblocage\"";
	}
}


