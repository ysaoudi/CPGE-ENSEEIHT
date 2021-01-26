// Time-stamp: <08 Apr 2008 11:35 queinnec@enseeiht.fr>

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import Synchro.Assert;

/** Lecteurs/rédacteurs
 * stratégie d'ordonnancement: priorité aux lecteurs,
 * implantation: avec un moniteur. */
public class LectRed_PrioLecteur implements LectRed
{
	
	private ReentrantLock Monitor = new ReentrantLock();
	private Condition readingIsPossible;
	private Condition writingIsPossible;
	private boolean isBeingWritten;
	private int nbrReadingRequests;
	private int nbrCurrentlyReading;
	
    public LectRed_PrioLecteur()
    {
    	readingIsPossible = Monitor.newCondition();
    	writingIsPossible = Monitor.newCondition();

    	nbrCurrentlyReading = 0;
    	nbrReadingRequests = 0;
    	
    	isBeingWritten = false;
    }

    public void demanderLecture() throws InterruptedException
    {
    	Monitor.lock();
    	try {
    		while (isBeingWritten) {
    			nbrReadingRequests++;
    			readingIsPossible.await();
    			nbrReadingRequests--;
    		}
    		nbrCurrentlyReading++;
    		readingIsPossible.signalAll();
    	} finally {
    		Monitor.unlock();
    	}
    }

    public void terminerLecture() throws InterruptedException
    {
    	Monitor.lock();
    	try {
    		nbrCurrentlyReading--;
    		if(nbrCurrentlyReading == 0 && nbrReadingRequests == 0) {
    			writingIsPossible.signal();
    		}
    	} finally {
    		Monitor.unlock();
    	}
    }

    public void demanderEcriture() throws InterruptedException
    {
    	Monitor.lock();
    	try {
    		while(isBeingWritten || nbrCurrentlyReading > 0 || nbrReadingRequests > 0) {
    			writingIsPossible.await();
    		}
    		isBeingWritten = true;
    	} finally {
    		Monitor.unlock();
    	}
    }

    public void terminerEcriture() throws InterruptedException
    {
    	Monitor.lock();
    	try {
    		isBeingWritten = false;
    		if(nbrReadingRequests == 0) {
    			writingIsPossible.signal();
    		} else {
    			readingIsPossible.signalAll();
    		}
    	} finally {
    		Monitor.unlock();
    	}
    }

    public String nomStrategie()
    {
        return "Stratégie: Priorité Lecteurs.";
    }
}
