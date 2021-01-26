package hdfs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class Connexion {
	
	private Socket sock; 
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public Connexion(Socket socket) {
		this.sock = socket;
		try {
			this.oos = new ObjectOutputStream(sock.getOutputStream());
			this.ois = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send(Object objet) {
		try {
			oos.writeObject(objet);
		} catch (UnknownHostException e) {
			System.out.println("Host inconnui à l'envoi");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object receive() {
		Object objet = null;
		try {
			objet = (Object) ois.readObject();
		} catch (UnknownHostException e) {
			System.out.println("Host non reconnu à la réception.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Objet inconnu à la réception");
			e.printStackTrace();
		}
		return objet;
	}
	
	public void Close() {
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}