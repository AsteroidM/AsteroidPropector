package EJBs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Singleton;
import resources.AsteroidJaxbBean;

/**
 *
 * @author Boris Feron
 */
@Singleton
public class AsteroidsEJB
{
    private Map<String, AsteroidJaxbBean> asteroidsDB;

    public Collection<AsteroidJaxbBean> getAsteroids()
    {
        return asteroidsDB.values();
//        List<AsteroidJaxbBean> list = new ArrayList<AsteroidJaxbBean>();
//        Collection<AsteroidJaxbBean> coll = asteroidsDB.values();
//        for(AsteroidJaxbBean asteroid : coll)
//        {
//            list.add(asteroid);
//        }
//        
//        return list;
    }

    public void resetEJB()
    {
        // Clear asteroids
        asteroidsDB.clear();
    }

    public AsteroidsEJB()
    {
        // Create and populate ConcurrentHashMap of users' mines.
        asteroidsDB = new ConcurrentHashMap<String, AsteroidJaxbBean>();
    }

    public void addAsteroid(AsteroidJaxbBean newAsteroid)
    {
        if(!asteroidsDB.containsKey(newAsteroid.name))
        {
            asteroidsDB.put(newAsteroid.name, newAsteroid);
        }
    }

    public boolean contains(String asteroidName)
    {
        return asteroidsDB.containsKey(asteroidName);
    }
    
    public AsteroidJaxbBean getAsteroid(String asteroidName)
    {
        return asteroidsDB.get(asteroidName);
    }
    
    public void setAsteroidOrbitSpeed(String asteroidName, double relativeSpeed)
    {
        AsteroidJaxbBean asteroid = asteroidsDB.get(asteroidName);
        asteroid.orbitSpeed = relativeSpeed;
    }
    
    public void setAsteroidDistance(String asteroidName, double relativeDistance)
    {
        AsteroidJaxbBean asteroid = asteroidsDB.get(asteroidName);
        asteroid.distance = relativeDistance;
    }
}
