import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.*;

public class CarnetImpl extends UnicastRemoteObject implements Carnet {	

	private static final long serialVersionUID = 1;
	
	Hashtable<String, RFiche> table = new Hashtable<String, RFiche>();
	int numCarnet;
	Carnet other = null;
	
	public CarnetImpl(int n) throws RemoteException {
		numCarnet = n;
	}

	public void Ajouter(SFiche sf) throws RemoteException {
		try {
			//System.out.println("CarnetImpl Ajouter ("+p.getNom()+","+p.getEmail()+")");
			RFiche rf = new RFicheImpl(sf.getNom(), sf.getEmail());
			table.put(sf.getNom(), rf);
		} catch (Exception e) {
			System.out.println("CarnetImpl error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public RFiche Consulter(String n, boolean forward) throws RemoteException {
		System.out.println("CarnetImpl: Consulter "+n);
		try {
			RFiche rf = table.get(n);
			if ((rf == null) && forward) {
				if (other == null) {
					other = (Carnet)Naming.lookup("//localhost:4000/Carnet"+(numCarnet%2+1));
					System.out.println("foward "+numCarnet+" >> "+ (numCarnet%2+1));
				}
				if (other != null) rf = other.Consulter(n, false);
			}
			if (rf==null) System.out.println("CarnetImpl: not found");
			return rf;
		} catch (Exception e) {
			System.out.println("CarnetImpl error: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static void main(String args[]) {
		try {
			Integer I = new Integer(args[0]);
			int numc = I.intValue();
			if ((numc != 1) && (numc != 2)) {
				System.out.println(" Usage: java CarnetImpl 1|2");
				return;
			}
			System.out.println(" Launching the registry");
/*
			try {
				Registry registry = LocateRegistry.createRegistry(4000);
			} catch (Exception ex) {
				System.out.println(" Probably the registry is already running ...");
			}
*/
			Naming.rebind("//localhost:4000/Carnet"+numc, new CarnetImpl(numc));
			System.out.println("Carnet"+numc+" bound in registry");
		} catch (Exception e) {
			System.out.println(" Usage: java CarnetImpl 1|2");
			e.printStackTrace();
		}
	}
}

