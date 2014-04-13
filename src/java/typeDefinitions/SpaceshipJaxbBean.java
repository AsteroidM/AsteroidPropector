package typeDefinitions;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Boris Feron
 */
@XmlRootElement
public class SpaceshipJaxbBean
{
    public String type;
    public int capacity;
    public int speed;
    public int creditCost;
    public int[] combinedCost;
 
    public SpaceshipJaxbBean() {} // JAXB needs this
 
    public SpaceshipJaxbBean(String type, int capacity, int speed, int creditCost, int[] combinedCost) {
        this.type = type;
        this.capacity = capacity;
        this.speed = speed;
        this.creditCost = creditCost;
        this.combinedCost = combinedCost;
    }
}
