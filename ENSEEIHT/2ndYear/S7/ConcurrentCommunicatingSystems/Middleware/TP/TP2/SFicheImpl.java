
// La classe SFicheImpl est serialisable
public class SFicheImpl implements SFiche {
	
	private static final long serialVersionUID = 1;

	String nom, email;
	public SFicheImpl (String n, String e) {
		nom = n;
		email = e;
	}
	public String getNom () {
		return nom;
	}
	public String getEmail () {
		return email;
	}
}



