package ordo;

import java.rmi.RemoteException;
import formats.Format;
import formats.Format.OpenMode;
import map.Mapper;
public class Mapthread extends Thread {
    Mapper m;
    Format reader;
    Format writer;
    CallBack cb;

    public Mapthread(Mapper mapper, Format reader, Format writer, CallBack cb) throws RemoteException{
        this.m = mapper;
        this.reader = reader;
        this.writer = writer;
        this.cb = cb;
    }

    public void run(){
        reader.open(OpenMode.R);
        writer.open(OpenMode.W);
        m.map(reader, writer);
        writer.close();
        reader.close();

        try {
            cb.LibererMap();
        } 
        catch( Exception e) {
            e.printStackTrace();
        }

       
    }

    
}
