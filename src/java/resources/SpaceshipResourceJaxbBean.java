package resources;

import typeDefinitions.SpaceshipJaxbBean;

/**
 *
 * @author Boris Feron
 */
public class SpaceshipResourceJaxbBean
{
    public String asteroidName;
    public String user;
    public int id;
    public SpaceshipJaxbBean spaceshipDefinition;
    public int destinationMineId;
    public int resourceCount;
    public long lastAccessTime;
    public double roundtripTime;
    public long lastDeliveryTime;
 
    public SpaceshipResourceJaxbBean() {} // JAXB needs this
 
    public SpaceshipResourceJaxbBean(SpaceshipJaxbBean spaceshipDefinition, int id, String user, int destinationMineId, int resourceCount, long lastAccessTime, long lastDeliveryTime) {
        this.spaceshipDefinition = spaceshipDefinition;
        this.id = id;
        this.user = user;
        this.destinationMineId = destinationMineId;
        this.resourceCount = resourceCount;
        this.lastAccessTime = lastAccessTime;
        this.lastDeliveryTime = lastDeliveryTime;
    }
    
    public void setRoundTripTime(double roundtripTime)
    {
        this.roundtripTime = roundtripTime;
    }
}