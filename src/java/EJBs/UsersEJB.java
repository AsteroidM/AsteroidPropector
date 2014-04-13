/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJBs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Singleton;
import typeDefinitions.UserJaxbBean;

/**
 *
 * @author boris
 */
@Singleton
public class UsersEJB
{
    // "enum types"
    public static int CREDITS = 0;
    public static int CARBON = 1;
    public static int IRONMAGNESIUMSILICATE = 2;
    public static int IRONNICKEL = 3;
    public static int SPECIALMATERIAL = 4;
    
    private Map<String, UserJaxbBean> usersDB;

    public UsersEJB()
    {
        usersDB = new ConcurrentHashMap<String, UserJaxbBean>();
    }

    public void resetEJB()
    {
        // Clear users
        usersDB.clear();
    }

    public void addUser(UserJaxbBean newUser)
    {
        if (!usersDB.containsKey(newUser.username))
        {
            usersDB.put(newUser.username, newUser);
        }
    }

    public boolean contains(String userName)
    {
        return usersDB.containsKey(userName);
    }

    public boolean hasResource(String userName, int resourceType, int amount)
    {
        UserJaxbBean user = getUser(userName);
        if (user == null)
        {
            return false;
        }
        if (resourceType==CREDITS)
        {
            return user.credits >= amount;
        }
        else if (resourceType==CARBON)
        {
            return user.carbon >= amount;
        }
        else if (resourceType==IRONMAGNESIUMSILICATE)
        {
            return user.ironMagnesiumSilicate >= amount;
        }
        else if (resourceType==IRONNICKEL)
        {
            return user.IronNickel >= amount;
        }
        else if (resourceType==SPECIALMATERIAL)
        {
            return user.specialMaterial >= amount;
        }
        else
        {
            return false;
        }
    }

    public UserJaxbBean getUser(String userName)
    {
        if (usersDB.containsKey(userName))
        {
            UserJaxbBean user = usersDB.get(userName);
            return user;
        }
        else
        {
            return null;
        }
    }
}
