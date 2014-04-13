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
@Path("SpaceshipData")
public class SpaceshipResource
{

    private final String IncorrectUser = "IncorrectUser";
    private final String IncorrectAsteroid = "IncorrectAsteroid";
    private final String IncorrectMineType = "IncorrectMineType";
    private final String IncorrectDepotType = "IncorrectDepotType";
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

//    @Path("CurrentSpaceshipResourceCount/{user}/{spaceshipId}")
//    @GET
//    @Produces("text/plain")
//    public String getCurrentSpaceshipResourceCount(@PathParam("user") String user, @PathParam("spaceshipId") int spaceshipId)
//    {
//        SpaceshipResourceJaxbBean spaceship = spaceshipResourceEJB.getSpaceship(spaceshipId);
//        int resourceMineCount = mineResourceEJB.getMineResourceCount(spaceship.destinationMineId);
//        
//    }

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
    @Path("CreateNewSpaceship/{user}/{spaceshipType}/{useOnlyCredits}")
    @GET
    @Produces("application/json")
    public String createNewSpaceship(@PathParam("user") String user, @PathParam("spaceshipType") int spaceshipTypeId, @PathParam("useOnlyCredits") boolean useOnlyCredits)
    {
        List<SpaceshipJaxbBean> spaceshipTypes = spaceshipResourceEJB.getSpaceshipTypes();

        if (usersEJB.contains(user))
        {
            if (spaceshipTypeId < spaceshipTypes.size())
            {
                SpaceshipJaxbBean spaceshipType = spaceshipTypes.get(spaceshipTypeId);
                int newSpaceshipId = spaceshipResourceEJB.addSpaceship(spaceshipType, user, 0);
                return createSimpleJSONResponse("Yes", newSpaceshipId + "");
            }
            else
            {
                return createSimpleJSONResponse("No", IncorrectMineType);
            }
        }
        else
        {
            return createSimpleJSONResponse("No", IncorrectUser + ":" + user);
        }
    }
    
    @Path("ChangeSpaceshipDestination/{user}/{spaceshipId}/{newMineDestinationId}")
    @GET
    @Produces("application/json")
    public String changeSpaceshipDestination(@PathParam("user") String user, @PathParam("spaceshipId") int spaceshipId, @PathParam("newMineDestinationId") int newMineDestinationId)
    {
        if(usersEJB.contains(user))
        {
            String oldAsteroidNameDestination = spaceshipResourceEJB.getAsteroidSpaceshipBelongsTo(spaceshipId);
            String newAsteroidNameDestination = mineResourceEJB.getAsteroidMineBelongsTo(newMineDestinationId);
            spaceshipResourceEJB.changeSpaceshipDestination(spaceshipId, newMineDestinationId, newAsteroidNameDestination);
            
            // Recalculate remaining resources for old and new asteroid destinations as both have changed
            recalculateAndPopulateNewRemainingAsteroidResources(oldAsteroidNameDestination);
            recalculateAndPopulateNewRemainingAsteroidResources(newAsteroidNameDestination);
            
            return createSimpleJSONResponse("yes", "destinationChanged");
        }
        else
        {
            return createSimpleJSONResponse("no", "destinationNotChanged");
        }
    }

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
    
    @Path("GetAllUsersSpaceships/{user}")
    @GET
    @Produces("application/json")
    public List<SpaceshipResourceJaxbBean> getAllUsersSpaceships(@PathParam("user") String userName)
    {
        return spaceshipResourceEJB.getAllUsersSpaceships(userName);
    }
}
