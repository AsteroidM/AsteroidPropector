package Services;

import EJBs.UsersEJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import typeDefinitions.UserJaxbBean;

/**
 *
 * @author Bors Feron
 */
@Stateless
@Path("UserData")
public class UserResource
{
    @Inject
    private UsersEJB usersEJB;
    
    @Path("GetCurrentUserResources/{user}")
    @GET
    @Produces("application/json")
    public UserJaxbBean getCurrentUserResources(@PathParam("user") String userName)
    {
        return usersEJB.getUser(userName);
    }
}
