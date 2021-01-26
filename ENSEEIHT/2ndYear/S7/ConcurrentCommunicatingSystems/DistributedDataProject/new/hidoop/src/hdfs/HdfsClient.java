package hdfs;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import config.InvalidPropertyException;
import config.Project;
import formats.Format;
import ordo.NameNodeInterface;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class HdfsClient {

	private static Project dataStructure;

	private static String nameNodeURL;

	private static int nbNodes;

	private static int tailleMaxFragment = 100;
	private static String strRecu;

	private static void usage() {
		System.out.println("Usage: java HdfsClient read <file>");
		System.out.println("Usage: java HdfsClient write <line|kv> <file>");
		System.out.println("Usage: java HdfsClient delete <file>");
	}

	public static void HdfsDelete(String hdfsFname) {
		String fSansExtension = hdfsFname.replaceFirst("[.][^.]+$", "");

		HashMap<Integer, String> mappingBlocs = dataStructure.daemonsFragmentRepartized.get(fSansExtension);

		mappingBlocs.forEach((i, url) -> {
			Socket sock;
			try {
				/*
				 * Pour recuperer le port, on cherche l'indice d'url dans notre liste d'url.
				 * Cette indice correspond au port dans la liste des ports.
				 */
				int nbNode = dataStructure.urlDaemons.indexOf(url);
				sock = new Socket(dataStructure.urlServ.get(nbNode), dataStructure.portNodes.get(nbNode));
				Connexion c = new Connexion(sock);
				Commande cmd = new Commande(Commande.Cmd.CMD_DELETE, Project.PATH + "hidoop/data/tmp/"+fSansExtension + "-bloc" + i + ".txt", 0);
				c.send(cmd);
				c.Close();
				dataStructure.daemonsFragmentRepartized.get(fSansExtension).remove(i);
				System.out.println("Suppresion de " + fSansExtension + "-bloc" + i + " sur le node " + nbNode);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		dataStructure.daemonsFragmentRepartized.remove(fSansExtension);
	}

	public static void HdfsWrite(Format.Type fmt, String localFSSourceFname, int repFactor) {

		File f = new File(Project.PATH + "hidoop/data/tmp/" + localFSSourceFname);
		String fSansExtension = localFSSourceFname.replaceFirst("[.][^.]+$", "");
		long tailleFichier = f.length();
		System.out.println("Taille du fichier à traiter :" + tailleFichier);
		Commande cmd = new Commande(Commande.Cmd.CMD_WRITE, "", 0);

		char[] buf = new char[tailleMaxFragment];
		int nbCaracEnvoye;
		int nbCaracPerdu = 0;
		int j;

		int nbFragment = (int) (tailleFichier / tailleMaxFragment);
		int reste = (int) (tailleFichier % tailleMaxFragment);
		System.out.println("Nombre de fragments requis : " + nbFragment);
		System.out.println("Caractère restant : " + reste);

		if (reste != 0) {
			System.out.println("Reste différent de 0 donc ajout d'un fragment.");
			nbFragment++;
		}

		try {
			FileReader fr = new FileReader(f);
			int node = 0;
			int i;
			HashMap<Integer, String> mappingBlocs = new HashMap<Integer, String>();

			for (i = 0; i < nbFragment; i++) {
				String addresseNode = dataStructure.urlServ.get(node);
				addresseNode = addresseNode.substring(2, addresseNode.length());
				Socket sock = new Socket(addresseNode, dataStructure.portNodes.get(node));
				Connexion c = new Connexion(sock);
				System.out.println("Ecriture du fragment n " + i + " sur le node " + node);
				System.out.println("Lecture...");
				for (int l = 0; l < nbCaracPerdu; l++) {
					buf[l] = buf[tailleMaxFragment - nbCaracPerdu + l];
				}

				fr.read(buf, nbCaracPerdu, tailleMaxFragment - nbCaracPerdu);

				j = tailleMaxFragment;
				nbCaracPerdu = 0;
				if (i != nbFragment) {
					while (buf[j - 1] != ' ' && j >= 1) {
						j--;
						nbCaracPerdu++;
					}
					reste += nbCaracPerdu;
					if (reste >= tailleMaxFragment) {
						reste -= tailleMaxFragment;
						nbFragment++;
					}
					nbCaracEnvoye = tailleMaxFragment - nbCaracPerdu;
					System.out.println("Lecture finie, fragment contenant : " + nbCaracEnvoye + " caractères.");

				} else {
					nbCaracEnvoye = reste;
					System.out.println("Fin du fichier atteinte, dernier fragment à envoyer, de taille : "
							+ nbCaracEnvoye + " caractères.");
				}

				System.out.println("perdu : " + nbCaracPerdu + " envoye : " + nbCaracEnvoye);
				cmd = new Commande(Commande.Cmd.CMD_WRITE, Project.PATH + "hidoop/data/tmp/"+  fSansExtension + "-bloc" + i + ".txt", nbCaracEnvoye);
				c.send(cmd);
				System.out.println("Envoi du fragment...");
				c.send(buf);

				mappingBlocs.put(i, dataStructure.urlDaemons.get(node));
				node++;
				if (node == nbNodes) {
					node = 0;
				}
				c.Close();
			}

			dataStructure.numberOfMaps.put(fSansExtension, nbFragment);

			System.out.println("Fin de l'ecriture " + i + " fragment ecrits.");
			dataStructure.daemonsFragmentRepartized.put(fSansExtension, mappingBlocs);

			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void HdfsRead(String hdfsFname, String localFSDestFname) {
		String fSansExtension = hdfsFname.replaceFirst("[.][^.]+$", "");

		HashMap<Integer, String> mappingBlocs = dataStructure.daemonsFragmentRepartized.get(hdfsFname);
		localFSDestFname = dataStructure.PATH + fSansExtension + "-concatenated.txt";
		File f = new File(localFSDestFname);
		FileWriter fw;

		try {
			strRecu = new String();

			fw = new FileWriter(f);
			mappingBlocs.forEach((i, url) -> {

				try {
					int nbNode = dataStructure.urlDaemons.indexOf(url);
					String addresseNode = dataStructure.urlServ.get(nbNode);
					addresseNode = addresseNode.substring(2, addresseNode.length());
					Socket sock = new Socket(addresseNode, dataStructure.portNodes.get(nbNode));
					Connexion c = new Connexion(sock);

					Commande cmd = new Commande(Commande.Cmd.CMD_READ,  Project.PATH + "hidoop/data/tmp/"+fSansExtension + "-res" + i + ".txt", 0);
					c.send(cmd);
					strRecu = strRecu + (String) c.receive();

					System.out.println("Lecture du fragment " + fSansExtension + "-res" + i + " sur le node " + nbNode);

					c.Close();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			fw.write(strRecu);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getNamNodeURL() {
		String res = null;
		try {
			FileInputStream in = new FileInputStream(Project.PATH + "hidoop/data/hdfsClient/namenode.url");
			Properties prop = new Properties();
			prop.load(in);
			in.close();

			res = prop.getProperty("url");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return res;

	}

	public static void main(String[] args) {

		try {
			NameNodeInterface nNI;

			nNI = (NameNodeInterface) Naming.lookup(getNamNodeURL());

			if (nNI.structureExists()) {
				System.out.println("Namenode existant : Recuperation.");
				dataStructure = nNI.recoverStructure();
			} else {
				System.out.println("Pas de namenode trouvé, création d'un nouveau.");
				dataStructure = new Project();
			}

			nbNodes = dataStructure.urlServ.size();

			if (args.length < 2) {
				usage();
				return;
			}

			switch (args[0]) {
			case "read":
				HdfsRead(args[1], null);
				break;
			case "delete":
				HdfsDelete(args[1]);
				break;
			case "write":

				Format.Type fmt;
				if (args.length < 3) {
					usage();
					return;
				}
				if (args[1].equals("line"))
					fmt = Format.Type.LINE;
				else if (args[1].equals("kv"))
					fmt = Format.Type.KV;
				else {
					usage();
					return;
				}
				HdfsWrite(fmt, args[2], 1);
			}

			/* Mise à jour du namenode */
			System.out.println(dataStructure.numberOfMaps.get(args[1]));
			nNI.updateStructure(dataStructure);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
