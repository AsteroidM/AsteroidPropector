package Services;

import EJBs.AsteroidsEJB;
import EJBs.MineResourceEJB;
import EJBs.SpaceshipResourceEJB;
import EJBs.UsersEJB;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import resources.MineResourceJaxbBean;
import typeDefinitions.MineJaxbBean;
import controllers.ResourceController;
import java.util.ArrayList;
import java.util.Collection;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import resources.AsteroidJaxbBean;
import resources.SpaceshipResourceJaxbBean;
import typeDefinitions.SpaceshipJaxbBean;

/**
 * REST Web Service
 *
 * @author Boris Feron
 */
@Stateless
@Path("MineData")
public class MineResource
{

    private final String IncorrectUser = "IncorrectUser";
    private final String IncorrectAsteroid = "IncorrectAsteroid";
    private final String IncorrectMineType = "IncorrectMineType";
    private final String IncorrectDepotType = "IncorrectDepotType";
    private final String InsufficientFunds = "InsufficientFunds";
    private final String MineDoesNotExistError = "MineDoesNotExistError";
    @Context
    private UriInfo context;
    @Inject
    private MineResourceEJB mineResourceEJB;
    @Inject
    private AsteroidsEJB asteroidsEJB;
    @Inject
    private UsersEJB usersEJB;
    @Inject
    private SpaceshipResourceEJB spaceshipResourceEJB;

    @Path("CurrentMineResourceCount/{user}/{mineId}")
    @GET
    @Produces("text/plain")
    public String getCurrentMineResourceCount(@PathParam("user") String user, @PathParam("mineId") int mineId)
    {
        Map<Integer, MineResourceJaxbBean> mineResources = mineResourceEJB.getMineResources();
        if (mineResources.containsKey(mineId))
        {
            String resourceCount = mineResourceEJB.getMineResourceCount(mineId) + "";
            return resourceCount;
        }
        else
        {
            return MineDoesNotExistError;
        }
    }

//    @Path("GetMineDefinition/{id}")
//    @GET
//    @Produces("application/json")
//    public MineJaxbBean getMineDefinition(@PathParam("id") int id)
//    {
//        List<MineJaxbBean> mineTypesList = mineResourceEJB.getMineTypes();
//        MineJaxbBean mineType = mineTypes.get(id);
//        return mineType;
//    }
    //@Path("CreateNewMine")
    //@POST
    //@Produces("application/json")
    //public String createNewMine(@FormParam("user") String user, @FormParam("asteroid") String asteroidName, @FormParam("mineType") int mineTypeId, @FormParam("useOnlyCredits") boolean useOnlyCredits)
    @Path("CreateNewMine/{user}/{asteroid}/{mineType}/{useOnlyCredits}")
    @GET
    @Produces("application/json")
    public String createNewMine(@PathParam("user") String user, @PathParam("asteroid") String asteroidName, @PathParam("mineType") int mineTypeId, @PathParam("useOnlyCredits") boolean useOnlyCredits)
    {
        List<MineJaxbBean> mineTypes = mineResourceEJB.getMineTypes();

        if (usersEJB.contains(user))
        {
            if (asteroidsEJB.contains(asteroidName))
            {
                if (mineTypeId < mineTypes.size())
                {
                    MineJaxbBean mineType = mineTypes.get(mineTypeId);
                    int newMineId = mineResourceEJB.addMine(mineType, user, asteroidName, useOnlyCredits);
                    recalculateAndPopulateNewRemainingAsteroidResources(asteroidName);

                    if (newMineId == 0)
                    {
                        return createSimpleJSONResponse("No", InsufficientFunds);
                    }
                    else
                    {
                        return createSimpleJSONResponse("Yes", newMineId + "");
                    }
                }
                else
                {
                    return createSimpleJSONResponse("No", IncorrectMineType);
                }
            }
            else
            {
                return createSimpleJSONResponse("No", IncorrectAsteroid);
            }
        }
        else
        {
            return createSimpleJSONResponse("No", IncorrectUser + ":" + user);
        }
    }

//    @Path("CreateNewSpaceship")
//    @POST
//    @Produces("application/json")
//    public String createNewSpaceship(@FormParam("user") String user, @FormParam("asteroid") int mineId, @FormParam("spaceshipTypeId") int spaceshipTypeId)
//    {
//        List<SpaceshipJaxbBean> spaceshipTypes = spaceshipResourceEJB.getSpaceshipTypes();
//
//        if (usersEJB.contains(user))
//        {
//            if (mineResourceEJB.contains(mineId))
//            {
//                if (spaceshipTypeId < spaceshipTypes.size())
//                {
//                    SpaceshipJaxbBean spaceshipType = spaceshipTypes.get(spaceshipTypeId);
//                    int newSpaceshipId = spaceshipResourceEJB.addSpaceship(spaceshipType, user, mineId);
//                    return createSimpleJSONResponse("Yes", newSpaceshipId + "");
//                }
//                else
//                {
//                    return createSimpleJSONResponse("No", IncorrectMineType);
//                }
//            }
//            else
//            {
//                return createSimpleJSONResponse("No", IncorrectAsteroid);
//            }
//        }
//        else
//        {
//            return createSimpleJSONResponse("No", IncorrectUser);
//        }
//    }
    private String createSimpleJSONResponse(String status, String answer)
    {
        if (status.equalsIgnoreCase("yes"))
        {
            return "{\"Success\":\"" + status + "\",\"id\":\"" + answer + "\"}";
        }
        else
        {
            return "{\"Success\":\"" + status + "\",\"ErrorCode\":\"" + answer + "\"}";
        }
    }

//    private int calculateRemainingAsteroidResources(String asteroidName)
//    {
//        
//    }
    @Path("GetAvailableMineTypes/{user}/{asteroidName}")
    @GET
    @Produces("application/json")
    public List<MineJaxbBean> getAvailableMineTypes(@PathParam("user") String userName, @PathParam("asteroidName") String asteroidName)
    {
        String asteroidType = asteroidsEJB.getAsteroid(asteroidName).type;

        return mineResourceEJB.getMineTypesByType(asteroidType);
    }

    @Path("GetAllUsersMines/{user}")
    @GET
    @Produces("application/json")
    public List<MineResourceJaxbBean> getAllUsersMines(@PathParam("user") String userName)
    {
        return mineResourceEJB.getAllUsersMines(userName);
    }

    @Path("GetAllUsersMinesOnAsteroid/{user}/{asteroid}")
    @GET
    @Produces("application/json")
    public List<MineResourceJaxbBean> getAllUsersMinesOnAsteroid(@PathParam("user") String userName, @PathParam("asteroid") String asteroidName)
    {
        return mineResourceEJB.getAllUsersMinesOnAsteroid(userName, asteroidName);
    }

    public void recalculateAndPopulateNewRemainingAsteroidResources(String asteroidName)
    {
        double totalRemainingAsteroidResource = asteroidsEJB.getAsteroid(asteroidName).mass;
        double totalEffectiveMiningSpeed = 0;
        double totalTime = 0;

        List<MineResourceJaxbBean> asteroidMines = new ArrayList<MineResourceJaxbBean>();

        for (MineResourceJaxbBean mine : mineResourceEJB.getMineResources().values())
        {
            if (mine.asteroidName.equalsIgnoreCase(asteroidName))
            {
                asteroidMines.add(mine);

                double effectiveMiningSpeed = 0.0;
                double spaceshipsCombinedMiningSpeed = 0;
                for (SpaceshipResourceJaxbBean spaceship : spaceshipResourceEJB.getSpaceshipResources().values())
                {
                    if (spaceship.destinationMineId == mine.id)
                    {
                        spaceshipsCombinedMiningSpeed = +(spaceship.spaceshipDefinition.capacity / spaceship.roundtripTime);
                    }
                }

                effectiveMiningSpeed = Math.min(mine.mineDefinition.miningSpeed, spaceshipsCombinedMiningSpeed);
                mine.effectiveMiningSpeed = effectiveMiningSpeed;
                totalEffectiveMiningSpeed = +effectiveMiningSpeed;
            }
        }

        totalTime = totalRemainingAsteroidResource / totalEffectiveMiningSpeed;

        for (MineResourceJaxbBean mine : asteroidMines)
        {
            mineResourceEJB.setRemainingResources(mine.id, (int) (totalTime * mine.effectiveMiningSpeed));
        }
    }
}
