package config;
import java.util.HashMap;

public class Project {
    
    //Hosts des workers

    //ports HDFSSERVER
    public static int ports[] = {8001, 8002, 8003};

    //public static String nameNodeURL = "//targaryen.enseeiht.fr:4000/NameNode";
    public static String nameNodeURL = "//localhost:4000/NameNode";

    //public static String hosts[] = {"r2d2.enseeiht.fr", "nickel.enseeiht.fr", "gandalf.enseeiht.fr"};
    public static String hosts[] = {"localhost", "localhost", "localhost"};

    //public static String workershosts[] = {"r2d2.enseeiht.fr", "nickel.enseeiht.fr", "gandalf.enseeiht.fr"};
    //Ports des workers
    public static int workersports [] = {8887,8888,8889};
}
