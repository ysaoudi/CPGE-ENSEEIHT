import java.util.concurrent.atomic.AtomicBoolean;

public class VersionPeterson {
    static int turn = 0;
    static boolean [] isInterested = {false,false};
    static Thread[] allThreads;

    public static void main(String[] args) {
        int numberOfThreads = 2;

        allThreads = new Thread[numberOfThreads];

        
        long depart = System.nanoTime();
        for (int i = 0; i < numberOfThreads; i++) {
            allThreads[i] = new Thread(new VersionPetersonProcesses(), String.valueOf(i));
            allThreads[i].start();
        }
        
        for (Thread thread : allThreads) {
            try {
                thread.join(); // permet d'attendre la terminaison de l'instance de thread auquel elle est
                               // appliquee
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }

        long fin = System.nanoTime();
        System.out.println("Durée d'execution: " + (fin - depart) / 1000000L);
    }
}

class VersionPetersonProcesses implements Runnable {
    int id, di;

    public void run() {
        id =  Integer.parseInt(Thread.currentThread().getName());
        di = (id+1) % 2;

        for (int j = 1; j < 3; j++) {
            executeThread();
            stopExecution();
        }
    }

    void executeThread() {
        System.out.println("Thread " + Thread.currentThread().getName() + " attend");
        VersionPeterson.isInterested[id] = true;
        VersionPeterson.turn = di ;
        while (( VersionPeterson.turn == di) && ( VersionPeterson.isInterested[di])) {
            //Wait
        }
        // Section Critique
        System.out.println("Thread " + Thread.currentThread().getName() + " en SC");
    }

    void stopExecution() {
        System.out.println("Thread " + Thread.currentThread().getName() + " hors SC\n");
        VersionPeterson.isInterested[id] = false;
    }
}
