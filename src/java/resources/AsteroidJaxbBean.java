package resources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Boris Feron
 */
@XmlRootElement
public class AsteroidJaxbBean {
    public String type;
    public String name;
    public String planetOrbit;
    public double actualDistance;
    public double actualOrbitSpeed;
    public double mass;
    public double distance;
    public double orbitSpeed;
    
 
    public AsteroidJaxbBean() {} // JAXB needs this
 
    public AsteroidJaxbBean(String name, String type, String planetOrbit, double actualDistance, double actualOrbitSpeed, double mass) {
        this.name = name;
        this.type = type;
        this.planetOrbit = planetOrbit;
        this.actualDistance = actualDistance;
        this.actualOrbitSpeed = actualOrbitSpeed;
        this.mass = mass;
    }
}