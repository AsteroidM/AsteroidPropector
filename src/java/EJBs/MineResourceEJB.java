package EJBs;

import controllers.ResourceController;
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
import typeDefinitions.UserJaxbBean;

/**
 *
 * @author Boris Feron
 */
@Singleton
public class MineResourceEJB
{

    @Inject
    private AsteroidsEJB asteroidsEJB;
    @Inject
    private SpaceshipResourceEJB spaceshipResourceEJB;
    @Inject
    private UsersEJB usersEJB;
    private List<MineJaxbBean> mineTypes;
    private Map<Integer, MineResourceJaxbBean> mineResourceDB;
    // Used for synchronized id creation
    // of resources.
    private AtomicInteger idMineTypeCounter;
    private AtomicInteger idMineCounter;

    public List<MineJaxbBean> getMineTypes()
    {
        return mineTypes;
    }

    public List<MineJaxbBean> getMineTypesByType(String type)
    {
        List<MineJaxbBean> mineTypesWithCorrectType = new ArrayList<MineJaxbBean>();

        for (MineJaxbBean mineType : mineTypes)
        {
            mineTypesWithCorrectType.add(mineType);
        }

        return mineTypes;
    }

    public Map<Integer, MineResourceJaxbBean> getMineResources()
    {
        return mineResourceDB;
    }

    public void resetEJB()
    {
        // Clear counters
        clearIds(idMineTypeCounter);
        clearIds(idMineCounter);

        // NOTE: Mine IDs must start from 1 as mineID = 0 is used for
        // spaceships that do not have a destination.
        // Also, if addMine resturns 0, user does not have enough money
        idMineCounter.getAndIncrement();

        // Clear mines
        mineResourceDB.clear();
    }

    private void clearIds(AtomicInteger idCounter)
    {
        idCounter.set(0);
    }

    public MineResourceEJB()
    {
        // Create new idCounters
        idMineTypeCounter = new AtomicInteger();
        idMineCounter = new AtomicInteger();

        // Create and populate list of types of mines.
        mineTypes = new ArrayList<MineJaxbBean>();

        // Create and populate ConcurrentHashMap of users' mines.
        mineResourceDB = new ConcurrentHashMap<Integer, MineResourceJaxbBean>();
    }

    public int getNewId(AtomicInteger idCounter)
    {
        return idCounter.getAndIncrement();
    }

    public int addMine(MineJaxbBean mineType, String user, String asteroidName, boolean useCreditCost)
    {
        boolean userHasEnoughResources = false;
        if (useCreditCost)
        {
            if (usersEJB.hasResource(user, UsersEJB.CREDITS, mineType.creditCost))
            {
                userHasEnoughResources = true;
            }
        }
        else
        {
            if (usersEJB.hasResource(user, UsersEJB.CREDITS, mineType.combinedCost[0]) && usersEJB.hasResource(user, mineType.enumType, mineType.combinedCost[1]))
            {
                userHasEnoughResources = true;
            }
        }
        
        if (userHasEnoughResources)
        {
            int newId = getNewId(idMineCounter);
            MineResourceJaxbBean mine = new MineResourceJaxbBean(mineType, newId, user, asteroidName, 0, System.currentTimeMillis(), 0);
            mineResourceDB.put(newId, mine);
            
            // Subtract the credits from the user's account
            if(useCreditCost)
            {
                UserJaxbBean userBean = usersEJB.getUser(user);
                userBean.credits = userBean.credits-mineType.creditCost;
            }
            else
            {
                UserJaxbBean userBean = usersEJB.getUser(user);
                userBean.credits = userBean.credits - mineType.combinedCost[0];
                if(mineType.enumType == UsersEJB.CARBON)
                {
                    userBean.carbon = userBean.carbon-mineType.combinedCost[1];
                }
                else if(mineType.enumType == UsersEJB.IRONMAGNESIUMSILICATE)
                {
                    userBean.ironMagnesiumSilicate = userBean.ironMagnesiumSilicate - mineType.combinedCost[1];
                }
                else if(mineType.enumType == UsersEJB.IRONNICKEL)
                {
                    userBean.IronNickel = userBean.IronNickel - mineType.combinedCost[1];
                }
                else if(mineType.enumType == UsersEJB.SPECIALMATERIAL)
                {
                    userBean.specialMaterial = userBean.specialMaterial - mineType.combinedCost[1];
                }
            }
            // Recalculate remaining asteroid resources for this asteroid
            //recalculateAndPopulateNewRemainingAsteroidResources(asteroidName);
            
            return newId;
        }
        else
        {
            return 0;
        }
    }

    public int addMineType(MineJaxbBean mineType)
    {
        int newId = getNewId(idMineTypeCounter);
        //mineTypes.put(newId, mineType);
        mineTypes.add(newId, mineType);

        return newId;
    }

    public boolean contains(int mineId)
    {
        return mineResourceDB.containsKey(mineId);
    }

    public void setRemainingResources(int mineId, int newRemainingValue)
    {
        mineResourceDB.get(mineId).remainingResources = newRemainingValue;
    }
    
    public String getAsteroidMineBelongsTo(int mineId)
    {
        return mineResourceDB.get(mineId).asteroidName;
    }
    
    public List<MineResourceJaxbBean> getAllUsersMines(String userName)
    {
        List<MineResourceJaxbBean> list = new ArrayList<MineResourceJaxbBean>();
        
        for(MineResourceJaxbBean mine : mineResourceDB.values())
        {
            if(mine.user.equalsIgnoreCase(userName))
            {
                list.add(mine);
            }
        }
        
        return list;
    }
    
    public List<MineResourceJaxbBean> getAllUsersMinesOnAsteroid(String userName, String asteroidName)
    {
        List<MineResourceJaxbBean> list = new ArrayList<MineResourceJaxbBean>();
        
        for(MineResourceJaxbBean mine : mineResourceDB.values())
        {
            if(mine.user.equalsIgnoreCase(userName) && mine.asteroidName.equalsIgnoreCase(asteroidName))
            {
                list.add(mine);
            }
        }
        
        return list;
    }
        
    public int getMineResourceCount(int mineId)
    {
        MineResourceJaxbBean mine = mineResourceDB.get(mineId);
        return Math.min(mine.resourceCount + ResourceController.calcResourcesMinedSinceLastCall(mine.lastAccessTime, mine.mineDefinition.miningSpeed), mine.mineDefinition.capacity);
    }
    
}
