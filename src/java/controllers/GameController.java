package controllers;

import EJBs.AsteroidsEJB;
import EJBs.MineResourceEJB;
import EJBs.SpaceshipResourceEJB;
import EJBs.UsersEJB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;
import resources.AsteroidJaxbBean;
import resources.MineResourceJaxbBean;
import resources.SpaceshipResourceJaxbBean;
import typeDefinitions.MineJaxbBean;
import typeDefinitions.UserJaxbBean;
import typeDefinitions.SpaceshipJaxbBean;

/**
 * REST Web Service
 *
 * @author Boris Feron
 */
@Stateless
@Path("GameController")
public class GameController
{
    // Scaling constants
    private final static double MAX_RELATIVE_SPEED = 0.02;
    private final static double MIN_RELATIVE_SPEED = 0.002;
    private final static double MAX_RELATIVE_DISTANCE = 100;
    private final static double MIN_RELATIVE_DISTANCE = 40;
    
    @Inject
    private AsteroidsEJB asteroidsEJB;
    @Inject
    private MineResourceEJB mineResourceEJB;
    @Inject
    private UsersEJB usersEJB;
    @Inject
    private SpaceshipResourceEJB spaceshipResourceEJB;

    @Path("CreateNewGame/{gameName}")
    //@PUT
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    //public String createNewGame(NewGameEntity newGameEntity)
    public String createNewGame(@PathParam("gameName") String gameName)
    {
        asteroidsEJB.resetEJB();
        mineResourceEJB.resetEJB();
        usersEJB.resetEJB();

        if (gameName.equalsIgnoreCase("standardGame"))
        {
            addNewUser("user1");
            addNewUser("user2");
            populateAsteroidDB();
            createMineTypes();
            createSpaceshipTypes();

            return "Standard game created with users:\nuser1\nuser2";
        }
        return "New game: '" + gameName + "' created";
    }

    @Path("AddNewUser/{userName}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String addNewUser(@PathParam("userName") String userName)
    {
        UserJaxbBean newUser = new UserJaxbBean(userName, 100, 0, 0, 0, 0);
        usersEJB.addUser(newUser);

        return "User: '" + newUser + "' added";
    }

    @Path("GetAsteroids")
    @GET
    @Produces("application/json")
    public Collection<AsteroidJaxbBean> getAsteroids() throws JAXBException
    {
        return asteroidsEJB.getAsteroids();
    }

    private void createMineTypes()
    {
        MineJaxbBean mineTypeS1 = new MineJaxbBean("S", 100, 5, 2, new int[]{2, 0});
        mineResourceEJB.addMineType(mineTypeS1);

        MineJaxbBean mineTypeS2 = new MineJaxbBean("S", 200, 10, 3, new int[]{3, 0});
        mineResourceEJB.addMineType(mineTypeS2);
    }
    
    private void createSpaceshipTypes()
    {
        SpaceshipJaxbBean spaceshipTypeS1 = new SpaceshipJaxbBean("S", 100, 5, 2, new int[]{2, 0});
        spaceshipResourceEJB.addSpaceshipType(spaceshipTypeS1);

        SpaceshipJaxbBean spaceshipTypeS2 = new SpaceshipJaxbBean("S", 200, 10, 3, new int[]{3, 0});
        spaceshipResourceEJB.addSpaceshipType(spaceshipTypeS2);        
    }

    private void populateAsteroidDB()
    {   
        AsteroidJaxbBean ceres = new AsteroidJaxbBean("001-Ceres", "C", "main", 2.77, 17.88, 9.43E20);
        AsteroidJaxbBean pallas = new AsteroidJaxbBean("002-Pallas", "B", "main", 2.77, 17.65, 2.11E20);
        AsteroidJaxbBean juno = new AsteroidJaxbBean("003-Juno", "S", "main", 2.67, 17.93, 2.67E19);
        // TODO: WRONG TYPE FOR VESTA
        AsteroidJaxbBean vesta = new AsteroidJaxbBean("004-Vesta", "B", "main", 2.36, 19.34, 2.59E20);
        AsteroidJaxbBean astraea = new AsteroidJaxbBean("005-Astraea", "S", "main", 2.57, 18.39, 2.90E18);
        AsteroidJaxbBean hebe = new AsteroidJaxbBean("006-Hebe", "S", "main", 2.46, 18.93, 1.28E19);
        AsteroidJaxbBean iris = new AsteroidJaxbBean("007-Iris", "S", "main", 2.39, 19.03, 1.62E19);
        AsteroidJaxbBean flora = new AsteroidJaxbBean("008-Flora", "S", "main", 2.20, 19.95, 8.47E18);
        AsteroidJaxbBean metis = new AsteroidJaxbBean("009-Metis", "S", "main", 2.39, 19.21, 1.47E19);
        AsteroidJaxbBean hygiea = new AsteroidJaxbBean("010-Hygiea", "C", "main", 3.14, 16.76, 8.67E19);
        AsteroidJaxbBean parthenope = new AsteroidJaxbBean("011-Parthenope", "S", "main", 2.45, 19.02, 6.15E18);
        AsteroidJaxbBean victoria = new AsteroidJaxbBean("012-Victoria", "S", "main", 2.33, 19.50, 2.45E18);
        AsteroidJaxbBean egeria = new AsteroidJaxbBean("013-Egeria", "G", "main", 2.58, 18.56, 1.63E19);
        AsteroidJaxbBean irene = new AsteroidJaxbBean("014-Irene", "S", "main", 2.59, 18.52, 8.20E18);
        AsteroidJaxbBean eunomia = new AsteroidJaxbBean("015-Eunomia", "S", "main", 2.64, 18.16, 3.12E19);
        AsteroidJaxbBean psyche = new AsteroidJaxbBean("016-Psyche", "M", "main", 2.92, 17.34, 2.27E19);
        AsteroidJaxbBean thetis = new AsteroidJaxbBean("017-Thetis", "S", "main", 2.47, 18.87, 1.20E18);
        AsteroidJaxbBean melpomene = new AsteroidJaxbBean("018-Melpomene" , "S", "main", 2.30, 19.42, 3.00E18);
        AsteroidJaxbBean fortuna = new AsteroidJaxbBean("019-Fortuna", "S", "main", 2.44, 18.94, 1.27E19);
        AsteroidJaxbBean massalia = new AsteroidJaxbBean("020-Massalia", "S", "main", 2.41, 19.09, 5.67E18);
        AsteroidJaxbBean lutetia = new AsteroidJaxbBean("021-Lutetia", "M", "main", 2.44, 18.96, 1.70E18);
        AsteroidJaxbBean kalliope = new AsteroidJaxbBean("022-Kalliope", "M", "main", 2.91, 17.42, 8.16E18);
        AsteroidJaxbBean thalia = new AsteroidJaxbBean("023-Thalia", "S", "main", 2.69, 18.12, 1.96E18);
        AsteroidJaxbBean themis = new AsteroidJaxbBean("024-Themis", "C", "main", 2.72, 16.76, 2.30E19);
        AsteroidJaxbBean phocaea = new AsteroidJaxbBean("025-Phocaea", "S", "main", 2.40, 18.91, 5.99E17);
        AsteroidJaxbBean proserpina = new AsteroidJaxbBean("026-Proserpina", "S", "main", 2.66, 18.24, 7.48E17);
        AsteroidJaxbBean euterpe = new AsteroidJaxbBean("027-Euterpe"   , "S", "main", 2.35, 19.29, 1.67E18);
        AsteroidJaxbBean bellona = new AsteroidJaxbBean("028-Bellona"   , "S", "main", 2.78, 17.77, 2.62E18);
        AsteroidJaxbBean amphitrite = new AsteroidJaxbBean("029-Amphitrite", "S", "main", 2.55, 18.61, 1.18E19);
        AsteroidJaxbBean urania = new AsteroidJaxbBean("030-Urania"    , "S", "main", 2.37, 19.28, 1.74E18);
        AsteroidJaxbBean euphrosyne = new AsteroidJaxbBean("031-Euphrosyne", "C", "main", 3.15, 16.57, 58.10E18);
        //AsteroidJaxbBean pomona = new AsteroidJaxbBean("032-Pomona"    , "S", "main", 2.37, 18.48, NA);
        //AsteroidJaxbBean polyhymnia = new AsteroidJaxbBean("033-Polyhymnia", "S", "main", 2.87, NA   , 6.20E18);
        AsteroidJaxbBean circe = new AsteroidJaxbBean("034-Circe"     , "C", "main", 2.69, 18.12, 3.66E18);
        //AsteroidJaxbBean leukothea = new AsteroidJaxbBean("035-Leukothea" , "C", "main", 2.99, 17.00, NA);
        AsteroidJaxbBean atalante = new AsteroidJaxbBean("036-Atalante"  , "C", "main", 2.74, 17.55, 4.32E18);
        AsteroidJaxbBean fides = new AsteroidJaxbBean("037-Fides"     , "S", "main", 2.64, 18.18, 1.30E18);
        AsteroidJaxbBean leda = new AsteroidJaxbBean("038-Leda"      , "C", "main", 2.74, 17.88, 5.71E18);
        AsteroidJaxbBean laetita = new AsteroidJaxbBean("039-Laetita"   , "S", "main", 2.77, 17.84, 4.72E18);
        AsteroidJaxbBean harmonia = new AsteroidJaxbBean("040-Harmonia"  , "S", "main", 2.27, 19.77, 1.30E18);
        AsteroidJaxbBean daphne = new AsteroidJaxbBean("041-Daphne"    , "C", "main", 2.76, 17.58, 6.80E18);
        AsteroidJaxbBean isis = new AsteroidJaxbBean("042-Isis"      , "S", "main", 2.44, 18.82, 1.58E18);
        AsteroidJaxbBean ariadne = new AsteroidJaxbBean("043-Ariadne"   , "S", "main", 2.20, 19.92, 1.21E18);
        AsteroidJaxbBean nysa = new AsteroidJaxbBean("044-Nysa", "E", "main", 2.42, 19.13, 3.70E17);
        AsteroidJaxbBean eugenia = new AsteroidJaxbBean("045-Eugenia"   , "F", "main", 2.72, 18.03, 5.69E18);
        AsteroidJaxbBean hestia = new AsteroidJaxbBean("046-Hestia"    , "C", "main", 2.53, 18.60, 3.50E18);
        AsteroidJaxbBean aglaja = new AsteroidJaxbBean("047-Aglaja"    , "C", "main", 2.88, 17.48, 3.25E18);
        AsteroidJaxbBean doris = new AsteroidJaxbBean("048-Doris"     , "C", "main", 3.11, 16.87, 1.70E19);
        //AsteroidJaxbBean pales = new AsteroidJaxbBean("049-Pales"     , "C", "main", 3.09, NA   , 2.69E18);
        //AsteroidJaxbBean virginia = new AsteroidJaxbBean("050-Virginia"  , "Ch", "main", 2.65, 17.91, 2.31E18)
        
        asteroidsEJB.addAsteroid(ceres);
        asteroidsEJB.addAsteroid(pallas);
        asteroidsEJB.addAsteroid(juno);
        asteroidsEJB.addAsteroid(vesta);
        asteroidsEJB.addAsteroid(astraea);
        asteroidsEJB.addAsteroid(hebe);
        asteroidsEJB.addAsteroid(iris);
        asteroidsEJB.addAsteroid(flora);
        asteroidsEJB.addAsteroid(metis);
        asteroidsEJB.addAsteroid(hygiea);
        asteroidsEJB.addAsteroid(parthenope);
        asteroidsEJB.addAsteroid(victoria);
        asteroidsEJB.addAsteroid(egeria);
        asteroidsEJB.addAsteroid(irene);
        asteroidsEJB.addAsteroid(eunomia);
        asteroidsEJB.addAsteroid(psyche);
        asteroidsEJB.addAsteroid(thetis);
        asteroidsEJB.addAsteroid(melpomene);
        asteroidsEJB.addAsteroid(fortuna);
        asteroidsEJB.addAsteroid(massalia);
        asteroidsEJB.addAsteroid(lutetia);
        asteroidsEJB.addAsteroid(kalliope);
        asteroidsEJB.addAsteroid(thalia);
        asteroidsEJB.addAsteroid(themis);
        asteroidsEJB.addAsteroid(phocaea);
        asteroidsEJB.addAsteroid(proserpina);
        asteroidsEJB.addAsteroid(bellona);
        asteroidsEJB.addAsteroid(euterpe);
        asteroidsEJB.addAsteroid(amphitrite);
        asteroidsEJB.addAsteroid(urania);
        asteroidsEJB.addAsteroid(euphrosyne);
        asteroidsEJB.addAsteroid(circe);
        asteroidsEJB.addAsteroid(atalante);
        asteroidsEJB.addAsteroid(fides);
        asteroidsEJB.addAsteroid(leda);
        asteroidsEJB.addAsteroid(laetita);
        asteroidsEJB.addAsteroid(harmonia);
        asteroidsEJB.addAsteroid(daphne);
        asteroidsEJB.addAsteroid(isis);
        asteroidsEJB.addAsteroid(ariadne);
        asteroidsEJB.addAsteroid(nysa);
        asteroidsEJB.addAsteroid(eugenia);
        asteroidsEJB.addAsteroid(hestia);
        asteroidsEJB.addAsteroid(aglaja);
        asteroidsEJB.addAsteroid(doris);
        
        setAllRelativeNumbers(asteroidsEJB);
        
    }
    
    private void setAllRelativeNumbers(AsteroidsEJB asteroidsEJB)
    {
        // Find minimum and maximum distances
        double maxDistance = 0.0;
        double minDistance = Double.MAX_VALUE;
        double maxSpeed = 0.0;
        double minSpeed = 0.0;
        
        for(AsteroidJaxbBean asteroid : asteroidsEJB.getAsteroids())
        {
            // Distance
            // Find max distance
            if(asteroid.actualDistance > maxDistance)
            {
                maxDistance = asteroid.actualDistance;
            }
            // Find min distance
            else if(asteroid.actualDistance < minDistance)
            {
                minDistance = asteroid.actualDistance;
            }
            
            // Speed
            // Find max speed
            if(asteroid.actualOrbitSpeed > maxSpeed)
            {
                maxSpeed = asteroid.actualOrbitSpeed;
            }
            // Find min speed
            else if(asteroid.actualOrbitSpeed < minSpeed)
            {
                minSpeed = asteroid.actualOrbitSpeed;
            }
        }
        
        for(AsteroidJaxbBean asteroid : asteroidsEJB.getAsteroids())
        {
            asteroidsEJB.setAsteroidOrbitSpeed(asteroid.name, getRelativeNumber(maxSpeed, minSpeed, MAX_RELATIVE_SPEED, MIN_RELATIVE_SPEED, asteroid.actualOrbitSpeed));
            asteroidsEJB.setAsteroidDistance(asteroid.name, getRelativeNumber(maxDistance, minDistance, MAX_RELATIVE_DISTANCE, MIN_RELATIVE_DISTANCE, asteroid.actualDistance));
        }        
    }
    private double getRelativeNumber(double max, double min, double newMax, double newMin, double val)
    {
        return (val-min)/(max-min)*(newMax-newMin)+newMin;
    }
}
