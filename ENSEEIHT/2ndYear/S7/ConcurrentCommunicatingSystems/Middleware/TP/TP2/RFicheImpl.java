
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

// La classe RFicheImpl est remote
public class RFicheImpl extends UnicastRemoteObject implements RFiche {
	
	private static final long serialVersionUID = 1;

	String nom, email;
	public RFicheImpl (String n, String e) throws RemoteException {
		nom = n;
		email = e;
		System.out.println("RFicheImpl: new: "+nom+" "+email);
	}
	public String getNom () throws RemoteException {
		System.out.println("RFicheImpl: getNom: "+nom);
		return nom;
	}
	public String getEmail () throws RemoteException {
		System.out.println("RFicheImpl: getEmail: "+email);
		return email;
	}
}



