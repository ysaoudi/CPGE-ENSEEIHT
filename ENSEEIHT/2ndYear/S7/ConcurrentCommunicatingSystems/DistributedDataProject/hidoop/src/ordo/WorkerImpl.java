package ordo;

import map.*;
import formats.*;

import java.net.InetAddress;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Naming;
import config.*;

public class WorkerImpl extends UnicastRemoteObject implements Worker {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public WorkerImpl() throws RemoteException {
    }
 
    @Override
    public void runMap (Mapper m, Format reader, Format writer, CallBack cb) throws RemoteException {
       
    try   { 
        Mapthread mt = new Mapthread(m, reader, writer, cb);
        mt.start();

    }
    catch (RemoteException r){
            r.printStackTrace();
    }

}


    public static void main(String[] args) {
        try {
            int registryport = Integer.parseInt(args[0]);
            //System.out.println("//" + InetAddress.getLocalHost().getHostName() + ':' + registryport + "/Worker");
            String s = InetAddress.getLocalHost().getHostName();
            WorkerImpl worker = new WorkerImpl();
            System.out.println("Starting the Worker on port " + registryport);
            Registry registry = LocateRegistry.createRegistry(registryport);
            int index =  getIndex(registryport);
            if(index == -1){
                System.out.println("INDEX OF PORT NOT FOUND!");
                return;
            }
            //Naming.rebind("//" + Project.workershosts[index] + ":" + Project.workersports[index] + "/Worker", worker);
            
            Naming.rebind("//" + s + ':' + registryport + "/Worker", worker);
        }// TO DO WHILE APPLYING ON N7 MACHINES ... InetAddress.getLocalHost().getHostName()
        catch (Exception e) {
            e.printStackTrace();

        }
    }

    private static int getIndex(int registryport) throws RemoteException {
        int index = -1;
        for ( int i = 0; i < Project.workersports.length; i++){
            //System.out.println(Project.workersports[i]);
            if(Project.workersports[i] == registryport){
                index = i;
                break;
            }
        }
        return index;
    }
}
