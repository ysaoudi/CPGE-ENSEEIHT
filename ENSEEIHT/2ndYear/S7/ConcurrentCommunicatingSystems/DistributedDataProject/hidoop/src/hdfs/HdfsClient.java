/* une PROPOSITION de squelette, incomplète et adaptable... */

package hdfs;

import java.io.*;
import java.net.*;
import java.rmi.Naming;
import config.Project;
import formats.*;
import formats.Format.OpenMode;

public class HdfsClient {

    //public static String nameNodeURL = "//targaryen.enseeiht.fr:4000/NameNode";
    //public static String nameNodeURL = "//localhost:4000/NameNode";
    //private static int ports[] = {8001, 8002, 8003};
    //TO DO: Use this:
    //private static String hosts[] = {"r2d2.enseeiht.fr", "nickel.enseeiht.fr", "gandalf.enseeiht.fr"};
    //private static String hosts[] = {"localhost", "localhost", "localhost"};
    private static int nbrServers = (Project.ports.length == Project.hosts.length)? Project.ports.length : -1;
    private static long maxChunkSize = 100;
    private static NameNode nameNode; 

    private static void usage() {
        System.out.println("Usage: java HdfsClient read <file>");
        System.out.println("Usage: java HdfsClient write <line|kv> <file>");
        System.out.println("Usage: java HdfsClient delete <file>");
    }
	
    public static void HdfsDelete(String hdfsFname) throws IOException {
        //TO DO: get nbrChunks of previously split file hdfsFName from namenode!
        boolean isKVFile = hdfsFname.endsWith("-res");
        int nbrChunks = nameNode.getNbrChunks(hdfsFname);
        if(nbrChunks == 0){
            System.out.println("File not found.. Exiting..");
            return;
        }

        for (int i = 0; i < nbrChunks; i++){
            int serverNumber = i % nbrServers;
            // 4 chunks : i = 0 -> serverNumber = 0
            // i = 1 -> serverNumber = 1
            // i = 2 -> serverNumber = 2 
            // i = 3 -> serverNumber = 0

            Socket socket = new Socket(Project.hosts[serverNumber], Project.ports[serverNumber]);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            String fileName = hdfsFname + (isKVFile? "" : "-chunk") + i;
            Command cmd = new Command(ECommandType.CMD_DELETE, fileName);
            // Send DELETE COMMAND to corresponding server
            oos.writeObject(cmd);
            oos.close();
            socket.close();
        }  
    }
	
    public static void HdfsRead(String hdfsFname, String localFSDestFname) throws IOException, ClassNotFoundException {
        //TO DO: get nbrChunks of previously split file hdfsFName from namenode!
        findNameNode();
        int nbrChunks = nameNode.getNbrChunks(hdfsFname);
        System.out.println( nbrChunks );
        if(nbrChunks == 0){
            System.out.println("File not found.. Exiting..");
            return;
        }

        String fileContent = "";
        for (int i = 0; i < nbrChunks; i++){
            int serverNumber = i % nbrServers;

            Socket socket = new Socket(Project.hosts[serverNumber], Project.ports[serverNumber]);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            //Get chunk result i (resulting from MapReduce as "test.txt-res1", "test.txt-res2", ...)
            String fileName = hdfsFname + "-res" + i;
            Command cmd = new Command(ECommandType.CMD_READ, fileName);
            //Send READ COMMAND to Server
            oos.writeObject(cmd);

            //Get Chunk from Server
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String chunk = (String) ois.readObject();
            fileContent += chunk;
            
            oos.close();
            ois.close();
            socket.close();
        }
        //Write final file result as "test.txt-res"
        FileWriter fileWriter = new FileWriter(new File(HdfsServer.PATH + "tmp/" + hdfsFname + "-res"));
        fileWriter.write(fileContent);
        fileWriter.close();
    }

    public static void HdfsWrite(Format.Type fmt, String localFSSourceFname, int repFactor) throws IOException {
        File file = new File(HdfsServer.PATH + "tmp/" + localFSSourceFname);
        int nbrChunks = (int) (file.length() / maxChunkSize);
        if((int) (file.length() % maxChunkSize) != 0){
            nbrChunks++;
        } 
        //int nbrChunks = 4;

        switch(fmt){
            case KV:
                HdfsWriteKV(localFSSourceFname, repFactor, nbrChunks);
                nameNode.addFile_ChunkToMap(localFSSourceFname + "-res", nbrChunks);
                break;
             case LINE:
                HdfsWriteLINE(localFSSourceFname, repFactor, nbrChunks);
                nameNode.addFile_ChunkToMap(localFSSourceFname, nbrChunks);
                break;
             default:
                break;
        }
    }

    public static void HdfsWriteKV(String localFSSourceFname, int repFactor, int nbrChunks) throws IOException {
        KVFormat file = new KVFormat(HdfsServer.PATH + "tmp/" + localFSSourceFname);
        file.open(OpenMode.R);

        for (int i = 0; i < nbrChunks; i++){
            KV kv = new KV();
            String chunk = "";
            int index = 0;

            //Get chunk i content
            kv = file.read();
            while (index < maxChunkSize && kv != null){
                chunk += kv.k + KV.SEPARATOR + kv.v + "\n";
                index = chunk.length();
                kv = file.read();
            }
            if(kv != null)
                chunk += kv.k + KV.SEPARATOR + kv.v;
            //Send WRITE COMMAND to Server along with the Chunk content
            int serverNumber = i % nbrServers;
            sendChunkToServer(localFSSourceFname + "-res", serverNumber, chunk, i, Format.Type.KV);

        }
        file.close();
    }

    public static void HdfsWriteLINE(String localFSSourceFname, int repFactor, int nbrChunks) throws IOException {
        LineFormat file = new LineFormat(HdfsServer.PATH + "tmp/" + localFSSourceFname);
        file.open(OpenMode.R);

        for (int i = 0; i < nbrChunks; i++){
            KV kv = new KV();
            String chunk = "";
            int index = 0;

            // Get chunk i content
            kv = file.read();
            while (index < maxChunkSize && kv != null){
                chunk += kv.v + "\n";
                index = (int) (file.getIndex() - i*maxChunkSize);
                kv = file.read();
            }
            if(kv != null)
                chunk += kv.v;
        
            //Send WRITE COMMAND to Server along with the Chunk content
            int serverNumber = i % nbrServers;
            sendChunkToServer(localFSSourceFname + "-chunk", serverNumber, chunk, i, Format.Type.LINE);

        }
        file.close();
    }

    private static void sendChunkToServer(String localFSSourceFname, int serverNumber, String chunk, int chunkNumber, Format.Type fmt)
            throws UnknownHostException, IOException {
        Socket socket = new Socket(Project.hosts[serverNumber], Project.ports[serverNumber]);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        String fileName = localFSSourceFname + chunkNumber;
        // Send WRITE COMMAND to corresponding server
        Command cmd = new Command(ECommandType.CMD_WRITE, fileName, fmt);
        cmd.setFileContent(chunk);
        oos.writeObject(cmd);
        oos.close();
        socket.close();
    }

    private static void findNameNode(){
        try {
            nameNode = (NameNode) Naming.lookup(Project.nameNodeURL);
            System.out.println("Namenode found...");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        // java HdfsClient <read|write> <line|kv> <file>
        if (nbrServers == -1) {
            System.out.println("PORTS AND SERVERS ARE NOT PROPARLY SET UP IN HdfsClient.java !");
            System.out.println("EXITING...");
            System.exit(0);
        }
        try {
            if (args.length<2) {usage(); return;}
            findNameNode();
            switch (args[0]) {
              case "read": HdfsRead(args[1],null); break;
              case "delete": HdfsDelete(args[1]); break;
              case "write": 
                Format.Type fmt;
                if (args.length<3) {usage(); return;}
                if (args[1].equals("line")) fmt = Format.Type.LINE;
                else if(args[1].equals("kv")) fmt = Format.Type.KV;
                else {usage(); return;}
                HdfsWrite(fmt,args[2],1);
            }	
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
