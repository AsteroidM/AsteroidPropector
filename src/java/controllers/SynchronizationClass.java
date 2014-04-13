/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.enterprise.context.ApplicationScoped;

/**
 * @author Boris Feron
 */
@ApplicationScoped
public class SynchronizationClass
{

    private Map<Integer, Lock> resourceLocks;
    private ReentrantLock lock = new ReentrantLock();
    
    public SynchronizationClass()
    {
        resourceLocks = new HashMap<Integer, Lock>();
    }
    
//    public Lock getLock(int lockId)
//    {
//        if (resourceLocks.containsKey(lockId))
//        {
//            return resourceLocks.get(lockId);
//        }
//        else
//        {
//            
//        }
//        
//    }
}
