package config;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;


public class Project implements Serializable {

	private static final long serialVersionUID = 1L;
	public static String currentPath = System.getProperty("user.dir"), currentPath_2 = currentPath.substring(0, currentPath.length() - 10);
    public static String PATH = currentPath_2;

	public List<String> urlServ = new ArrayList<String>();


	public List<String> urlDaemons = new ArrayList<String>();

	public List<Integer> portNodes = new ArrayList<Integer>();

	public HashMap<String, Integer> numberOfMaps = new HashMap<String, Integer>();

	public HashMap<String, HashMap<Integer, String>> daemonsFragmentRepartized = new HashMap<String, HashMap<Integer, String>>();

	public List<String> inputFileNameList = new ArrayList<String>();

	public Project() {
		try {
			getStructure();
		} catch (InvalidPropertyException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @throws InvalidPropertyException
	 */
	public void getStructure() throws InvalidPropertyException {

		Properties propConf = new Properties();
		Properties propDaemons = new Properties();
		Properties propPorts = new Properties();
		Properties propServ = new Properties();
		int numberOfFile = 0;

		try {
			FileInputStream isPorts = new FileInputStream(Project.PATH + "hidoop/data/hdfsClient/portNodes.conf");
			FileInputStream isConf = new FileInputStream(Project.PATH + "hidoop/data/hdfsClient/structure.conf");
			FileInputStream isDaemons = new FileInputStream(Project.PATH + "hidoop/data/hdfsClient/daemons.listofurl");
			FileInputStream isServ = new FileInputStream(Project.PATH + "hidoop/data/hdfsClient/servHdfs.listofurl");

			propDaemons.load(isDaemons);
			isDaemons.close();

			propServ.load(isServ);
			isServ.close();

			propConf.load(isConf);
			isConf.close();

			propPorts.load(isPorts);
			isPorts.close();

			String property = propConf.getProperty("fileName0");

			while (property != null) {
				numberOfFile++;
				property = propConf.getProperty("fileName" + numberOfFile);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);

		} finally {
		
			for (int i = 0; i < numberOfFile; i++) {
				inputFileNameList.add(propConf.getProperty("fileName" + i));
			}
		
			int nbDaemons = 0;

			String daemon = propDaemons.getProperty("url0");

			while (daemon != null) {
				urlDaemons.add(daemon);
				
				nbDaemons++;
				daemon = propDaemons.getProperty("url" + nbDaemons);
			}

			int nbServ = 0;

			String serv = propServ.getProperty("url0");

			while (serv != null) {
				urlServ.add(serv);
				
				nbServ++;
				serv = propServ.getProperty("url" + nbServ);
			}

		
			int nbPorts = 0;

			String port = propPorts.getProperty("port0");

			while (port != null) {
				portNodes.add(Integer.parseInt(port));
				
				nbPorts++;
				port = propPorts.getProperty("port" + nbPorts);
			}

			for (int i = 0; i < inputFileNameList.size(); i++) {
				System.out.println(inputFileNameList.get(i));
				if (inputFileNameList.get(i) == null) {
					throw new InvalidPropertyException("structure.inputFileNameList.get(" + i + ")");
				}
			}
		}
	}
}