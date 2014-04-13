/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

/**
 *
 * @author Boris Feron
 */
public class ResourceController
{
    private static long calcTimeSinceLastAccess(long lastAccessTime)
    {
        return System.currentTimeMillis() - lastAccessTime;
    }
    
    public static int calcResourcesMinedSinceLastCall(long lastAccessTime, int miningSpeed)
    {
        int time = (int)calcTimeSinceLastAccess(lastAccessTime)/1000;
        
        return time*miningSpeed;
    }
}
