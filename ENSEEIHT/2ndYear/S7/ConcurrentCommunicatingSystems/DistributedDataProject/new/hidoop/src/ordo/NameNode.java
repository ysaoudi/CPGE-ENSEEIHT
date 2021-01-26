package ordo;


import java.net.*;

import config.Project;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.*;

import java.util.HashMap;

public class NameNode extends UnicastRemoteObject implements NameNodeInterface {

	private static final long serialVersionUID = 1L;
	private static String URL;
	private Project structure;
	private static int port = 8000;

	public NameNode() throws RemoteException {
	}
	
	public void updateStructure(Project struct) throws RemoteException {
		structure = struct;
		System.out.println("Update structure : Reception d'une nouvelle structure par HDFSClient.");
	}
	public Project recoverStructure() throws RemoteException {
		System.out.println("Requete de récuperation de structure par HDFSClient");
		return structure;
	}

	public boolean structureExists() throws RemoteException{
		return (structure != null);
	}

	public int getNumberOfMaps(String inputFname) throws RemoteException {
		return structure.numberOfMaps.get(inputFname);
	}

	public HashMap<Integer, String> getDaemonsURL(String inputFname) throws RemoteException {
		return structure.daemonsFragmentRepartized.get(inputFname);
	}
	

	public static void main(String args[]) {

		try {
			NameNode nn = new NameNode();

			URL = "//" + InetAddress.getLocalHost().getHostName() + ":" + port + "/NameNode";

			try {
				LocateRegistry.getRegistry(port);
				Naming.rebind(URL, nn);
				System.out.println("NameNode create at URL : " + URL + ", registry existant");

			} catch (Exception e) {
				try {
					LocateRegistry.createRegistry(port);
					Naming.rebind(URL, nn);
					System.out.println("NameNode create at URL : " + URL + ", création registry");

				} catch (Exception ex) {
					ex.printStackTrace();
					System.exit(0);
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(0);
		}

	}

}
