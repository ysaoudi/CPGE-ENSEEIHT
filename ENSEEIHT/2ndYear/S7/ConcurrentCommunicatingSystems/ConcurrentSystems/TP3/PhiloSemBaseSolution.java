// Time-stamp: <08 déc 2009 08:30 queinnec@enseeiht.fr>

import java.util.concurrent.Semaphore;
	
public class PhiloSemBaseSolution implements StrategiePhilo {


	/****************************************************************/
	static Semaphore[] availableForks;

	public PhiloSemBaseSolution(int nbPhilosophes) {
		availableForks = new Semaphore[nbPhilosophes];

		for (int i = 0; i < nbPhilosophes; i++) {
			availableForks[i] = new Semaphore(1);
		}
	}

	/**
	 * Le philosophe no demande les fourchettes. Pr�condition : il n'en poss�de
	 * aucune. Postcondition : quand cette m�thode retourne, il poss�de les deux
	 * fourchettes adjacentes � son assiette.
	 */
	public void demanderFourchettes(int no) throws InterruptedException {
		if (no == Main.nbPhilosophes - 1) {
			availableForks[Main.FourchetteGauche(no)].acquire();
			availableForks[Main.FourchetteDroite(no)].acquire();
		} else {
			availableForks[Main.FourchetteDroite(no)].acquire();
			availableForks[Main.FourchetteGauche(no)].acquire();
		}
	}

	/**
	 * Le philosophe no rend les fourchettes. Pr�condition : il poss�de les deux
	 * fourchettes adjacentes � son assiette. Postcondition : il n'en poss�de
	 * aucune. Les fourchettes peuvent être libres ou r�attribu�es � un autre
	 * philosophe.
	 */
	public void libererFourchettes(int no) {
		availableForks[Main.FourchetteDroite(no)].release();
		availableForks[Main.FourchetteGauche(no)].release();
	}

	/** Nom de cette stratégie (pour la fenêtre d'affichage). */
	public String nom() {
		return "Implantation S�maphores, strat�gie \"Fourchettes en tant que ressources critiques: Solution 1 de l'interblocage\"";
	}
}


