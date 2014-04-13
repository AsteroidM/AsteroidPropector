package typeDefinitions;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Boris Feron
 */
@XmlRootElement
public class MineJaxbBean {
    public String type;
    public int capacity;
    public int miningSpeed;
    public int creditCost;
    public int[] combinedCost;
    public int enumType;
    
    // "enum types"
    public static int CARBON = 1;
    public static int IRONMAGNESIUMSILICATE = 2;
    public static int IRONNICKEL = 3;
    public static int SPECIALMATERIAL = 4;
 
    public MineJaxbBean() {} // JAXB needs this
 
    public MineJaxbBean(String type, int capacity, int miningSpeed, int creditCost, int[] combinedCost) {
        this.type = type;
        this.capacity = capacity;
        this.miningSpeed = miningSpeed;
        this.creditCost = creditCost;
        this.combinedCost = combinedCost;
        setCorrectSuperTypeAndEnum(type);
    }
    
    private final void setCorrectSuperTypeAndEnum(String type)
    {
        if(type.equalsIgnoreCase("S") || type.equalsIgnoreCase("A") || type.equalsIgnoreCase("K") || type.equalsIgnoreCase("L") || type.equalsIgnoreCase("Q") || type.equalsIgnoreCase("R"))
        {
            this.type = "S";
            enumType = IRONMAGNESIUMSILICATE;
        }
        else if(type.equalsIgnoreCase("C") || type.equalsIgnoreCase("B") || type.equalsIgnoreCase("F") || type.equalsIgnoreCase("G"))
        {
            enumType = CARBON;
        }
        else if(type.equalsIgnoreCase("X") || type.equalsIgnoreCase("E") || type.equalsIgnoreCase("M") || type.equalsIgnoreCase("P"))
        {
            this.type = "X";
            enumType = IRONNICKEL;
        }        
    }
}