package entities;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Boris Feron
 */
@XmlRootElement
public class NewGameEntity
{
    @XmlElement(name = "gameName")
    private String gameName;
    @XmlElement(name = "users")
    private List<String> users;

    public List<String> getUsers()
    {
        return users;
    }

    public void setUsers(List<String> users)
    {
        this.users = users;
    }
}
