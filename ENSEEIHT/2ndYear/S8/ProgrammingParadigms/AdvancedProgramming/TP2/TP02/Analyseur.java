import java.io.*;
import java.util.*;

/** Analyser des donnÃ©es d'un fichier, une donnÃ©e par ligne avec 4 informations
 * sÃ©parÃ©es par des blancs : x, y, ordre (ignorÃ©e), valeur.
 */
public class Analyseur {
	/** Conserve la somme des valeurs associÃ©es Ã  une position. */
	private Map<Position, Double> cumuls;

	/** Construire un analyseur vide. */
	public Analyseur() {
		cumuls = new HashMap<>();
	}

	/**
	 * Charger l'analyseur avec les donnÃ©es du fichier "donnees.java".
	 * 
	 * @throws MalformedFileException
	 */
	public void charger(String fileName) throws MalformedFileException {
		try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
			boolean isFileF2Txt = fileName.substring(fileName.length() - 7, fileName.length()).equals("-f2.txt") ;
			int xIndex = isFileF2Txt ? 1 : 0;
			int yIndex = isFileF2Txt ? 2 : 1;
			int valIndex = isFileF2Txt ?  4 : 3;
			String ligne = null;
			while ((ligne = in.readLine()) != null) {
				String[] mots = ligne.split("\\s+");
				assert mots.length == 4;	// 4 mots sur chaque ligne
				int x = Integer.parseInt(mots[xIndex]);
				int y = Integer.parseInt(mots[yIndex]);
				//if(x < 0 || y < 0) throw new MalformedFileException();
				Position p = new Position(x, y);
				double valeur = Double.parseDouble(mots[valIndex]);
				if(valeur < 0 ) throw new MalformedFileException();
				cumuls.put(p, valeur(p) + valeur);
				// p.setY(p.getY() + 1);	//  p.y += 1;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/** Obtenir la valeur associÃ©e Ã  une position. */
	public double valeur(Position position) {
		Double valeur = cumuls.get(position);
		return valeur == null ? 0.0 : valeur;
	}

	/** Obtenir toutes les donnÃ©es. */
	public Map<Position, Double> donnees() {
		return Collections.unmodifiableMap(this.cumuls);
	}

	/** Affichier les donnÃ©es. */
	public static void main(String[] args) {
		if(args.length == 0) return;

		Analyseur a = new Analyseur();
		for (int i = 0; i < args.length; i ++)
		{
			try {
				a.charger(args[i]);
			} catch (MalformedFileException e) {
				System.out.println("Valeurs négatives!");
				System.exit(-1);
			}
			System.out.println("------------------------------- Fichier " + args[i] + " -------------------------------");
			System.out.println(a.donnees());
			System.out.println("Nombres de positions : " + a.donnees().size());
			System.out.println("------------------------------------------------------------------------------------\n");
		}
	}
}
