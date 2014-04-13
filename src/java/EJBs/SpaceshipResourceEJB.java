package EJBs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import javax.inject.Singleton;
import resources.MineResourceJaxbBean;
import resources.SpaceshipResourceJaxbBean;
import typeDefinitions.MineJaxbBean;
import typeDefinitions.SpaceshipJaxbBean;
import typeDefinitions.UserJaxbBean;

/**
 *
 * @author Boris Feron
 */
@Singleton
public class SpaceshipResourceEJB
{

    @Inject
    private UsersEJB usersEJB;
    private List<SpaceshipJaxbBean> spaceshipTypes;
    private Map<Integer, SpaceshipResourceJaxbBean> spaceshipResourceDB;
    // Used for synchronized id creation
    // of resources.
    private AtomicInteger idSpaceshipTypeCounter;
    private AtomicInteger idSpaceshipCounter;

    public List<SpaceshipJaxbBean> getSpaceshipTypes()
    {
        return spaceshipTypes;
    }

    public Map<Integer, SpaceshipResourceJaxbBean> getSpaceshipResources()
    {
        return spaceshipResourceDB;
    }

    public void resetEJB()
    {
        // Clear counters
        clearIds(idSpaceshipTypeCounter);
        clearIds(idSpaceshipCounter);

        // NOTE: Mine IDs must start from 1 as mineID = 0 is used for
        // spaceships that do not have a destination.
        // Also, if addMine resturns 0, user does not have enough money
        idSpaceshipCounter.getAndIncrement();

        // Clear mines
        spaceshipResourceDB.clear();
    }

    private void clearIds(AtomicInteger idCounter)
    {
        idCounter.set(0);
    }

    public SpaceshipResourceEJB()
    {
        // Create new idCounters
        idSpaceshipTypeCounter = new AtomicInteger();
        idSpaceshipCounter = new AtomicInteger();

        // Create and populate list of types of mines.
        spaceshipTypes = new ArrayList<SpaceshipJaxbBean>();

        // Create and populate ConcurrentHashMap of users' mines.
        spaceshipResourceDB = new ConcurrentHashMap<Integer, SpaceshipResourceJaxbBean>();
    }

    public int getNewId(AtomicInteger idCounter)
    {
        return idCounter.getAndIncrement();
    }

    public int addSpaceship(SpaceshipJaxbBean spaceshipType, String user, int mineId)
    {
        if (usersEJB.hasResource(user, UsersEJB.CREDITS, spaceshipType.creditCost))
        {
            int newId = getNewId(idSpaceshipCounter);
            long currentTime = System.currentTimeMillis();
            SpaceshipResourceJaxbBean spaceship = new SpaceshipResourceJaxbBean(spaceshipType, newId, user, mineId, 0, currentTime, currentTime);
            spaceshipResourceDB.put(newId, spaceship);

            return newId;
        }
        else
        {
            return 0;
        }
    }

    public boolean changeSpaceshipDestination(int spaceshipId, int mineId, String asteroidName)
    {
        if (spaceshipResourceDB.containsKey(spaceshipId))
        {
            SpaceshipResourceJaxbBean spaceship = spaceshipResourceDB.get(spaceshipId);
            spaceship.destinationMineId = mineId;
            spaceship.asteroidName = asteroidName;
            
            return true;
        }
        else
        {
            return false;
        }
    }

    public int addSpaceshipType(SpaceshipJaxbBean spaceshipType)
    {
        int newId = getNewId(idSpaceshipTypeCounter);
        spaceshipTypes.add(newId, spaceshipType);

        return newId;
    }
    
    public String getAsteroidSpaceshipBelongsTo(int spaceshipId)
    {
        return spaceshipResourceDB.get(spaceshipId).asteroidName;
    }
    
    public SpaceshipResourceJaxbBean getSpaceship(int spaceshipId)
    {
        return spaceshipResourceDB.get(spaceshipId);
    }
    
    public List<SpaceshipResourceJaxbBean> getAllUsersSpaceships(String userName)
    {
        List<SpaceshipResourceJaxbBean> list = new ArrayList<SpaceshipResourceJaxbBean>();
        
        for(SpaceshipResourceJaxbBean spaceship : spaceshipResourceDB.values())
        {
            if(spaceship.user.equalsIgnoreCase(userName))
            {
                list.add(spaceship);
            }
        }
        
        return list;
    }
}
