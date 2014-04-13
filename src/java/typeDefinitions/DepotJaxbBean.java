package typeDefinitions;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Boris Feron
 */
@XmlRootElement
public class DepotJaxbBean {
    public String type;
    public int capacity;
 
    public DepotJaxbBean() {} // JAXB needs this
 
    public DepotJaxbBean(String type, int capacity) {
        this.type = type;
        this.capacity = capacity;
    }
}