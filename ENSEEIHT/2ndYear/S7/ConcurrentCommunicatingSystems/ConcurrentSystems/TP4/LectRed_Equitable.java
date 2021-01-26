// Time-stamp: <08 Apr 2008 11:35 queinnec@enseeiht.fr>

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import Synchro.Assert;

/** Lecteurs/rédacteurs
 * stratégie d'ordonnancement: Priorités Egales,
 * implantation: avec un moniteur. */
public class LectRed_Equitable implements LectRed
{
	
	private ReentrantLock Monitor = new ReentrantLock();
	private Condition sas;
	private Condition accessIsPossible;
	private boolean isBeingWritten;
	private int nbrCurrentlyReading;
	
    public LectRed_Equitable()
    {
    	sas = Monitor.newCondition();
    	accessIsPossible = Monitor.newCondition();

    	nbrCurrentlyReading = 0;
    	isBeingWritten = false;
    }

    public void demanderLecture() throws InterruptedException
    {
    	Monitor.lock();
    	try {
    		if (isBeingWritten || Monitor.hasWaiters(sas) || Monitor.hasWaiters(accessIsPossible)) {
    			accessIsPossible.await();
    		}
    		nbrCurrentlyReading++;
    	} finally {
    		Monitor.unlock();
    	}
    }

    public void terminerLecture() throws InterruptedException
    {
    	Monitor.lock();
    	try {
    		nbrCurrentlyReading--;
    		if(nbrCurrentlyReading == 0) {
    			if(Monitor.hasWaiters(sas)) {
        			sas.signal();
        		} else {
        			accessIsPossible.signal();
        		}
    		}
    	} finally {
    		Monitor.unlock();
    	}
    }

    public void demanderEcriture() throws InterruptedException
    {
    	Monitor.lock();
    	try {
    		if(isBeingWritten || nbrCurrentlyReading > 0 || Monitor.hasWaiters(accessIsPossible) || Monitor.hasWaiters(sas)) {
    			accessIsPossible.await();
    		}
    		if(nbrCurrentlyReading > 0) {
    			sas.await();
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
    		accessIsPossible.signal();
 
    	} finally {
    		Monitor.unlock();
    	}
    }

    public String nomStrategie()
    {
        return "Stratégie: Equitable.";
    }
}
