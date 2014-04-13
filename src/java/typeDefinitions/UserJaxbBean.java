package typeDefinitions;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Boris Feron
 */
@XmlRootElement
public class UserJaxbBean
{
    public String username;
    public int credits;
    public int carbon;
    public int ironMagnesiumSilicate;
    public int IronNickel;
    public int specialMaterial;   
    
    public UserJaxbBean()
    {
    } // JAXB needs this

    public UserJaxbBean(String username,int credits, int carbon, int ironMagnesiumSilicate, int IronNickel, int specialMaterial)
    {
        this.username = username;
        this.credits = credits;
        this.carbon = carbon;
        this.ironMagnesiumSilicate = ironMagnesiumSilicate;
        this.IronNickel = IronNickel;
        this.specialMaterial = specialMaterial;
    }
    
}