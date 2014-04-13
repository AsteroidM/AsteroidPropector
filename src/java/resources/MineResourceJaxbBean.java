package resources;

import javax.xml.bind.annotation.XmlRootElement;
import typeDefinitions.MineJaxbBean;

/**
 * @author Boris Feron
 */
@XmlRootElement
public class MineResourceJaxbBean{
    public String user;
    public int id;
    public MineJaxbBean mineDefinition;
    public String asteroidName;
    public int resourceCount;
    public long lastAccessTime;
    public double effectiveMiningSpeed;
    public int remainingResources;
 
    public MineResourceJaxbBean() {} // JAXB needs this
 
    public MineResourceJaxbBean(MineJaxbBean mineDefinition, int id, String user, String asteroidName, int resourceCount, long lastAccessTime, int remainingResources) {
        this.mineDefinition = mineDefinition;
        this.id = id;
        this.user = user;
        this.asteroidName = asteroidName;
        this.resourceCount = resourceCount;
        this.lastAccessTime = lastAccessTime;
        this.remainingResources = remainingResources;
    }
    
    public void setRemainingResource(int remainingResources)
    {
        this.remainingResources = remainingResources;
    }
}