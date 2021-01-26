import java.util.concurrent.atomic.AtomicBoolean;

public class TestAndSet {
    static AtomicBoolean isBusy = new AtomicBoolean(false);
    static Thread[] allThreads;

    public static void main(String[] args) {
        int numberOfThreads = 3;

        if (args.length >= 1) {
            try {
                numberOfThreads = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Arguments invalides. Execution avec 3 activites.");
            }
        } else {
            System.out.println("Nb d'arguments ≠ 1. Execution avec 3 activites.");
        }

        allThreads = new Thread[numberOfThreads];

        
        long depart = System.nanoTime();
        for (int i = 0; i < numberOfThreads; i++) {
            allThreads[i] = new Thread(new TestAndSetProcesses(), String.valueOf(i));
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

class TestAndSetProcesses implements Runnable {
    public void run() {
        for (int j = 1; j < 3; j++) {
            executeThread();
            stopExecution();
        }
    }

    void executeThread() {
        System.out.println("Thread " + Thread.currentThread().getName() + " attend");
        while (TestAndSet.isBusy.getAndSet(true)) {
            // Wait
        }
        // Section Critique
        System.out.println("Thread " + Thread.currentThread().getName() + " en SC");
    }

    void stopExecution() {
        System.out.println("Thread " + Thread.currentThread().getName() + " hors SC\n");
        TestAndSet.isBusy.set(false);
    }
}
