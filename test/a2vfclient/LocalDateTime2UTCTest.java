package a2vfclient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Programme de tests pour le projet A2VFClient
 * 
 * @author Thierry Baribaud
 * @version 1.0.15
 */
public class LocalDateTime2UTCTest {

    public LocalDateTime2UTCTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * On recherhche la méthode pour convertir le temps local en UTC ...
     */
    @Test
    public void localDateTime2UTCTest() {
        
        DateTimeFormatter UTC8601DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateTimeFormatter ISO8601DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                
        LocalDateTime localDateTime;
//        localDateTime = LocalDateTime.parse(localDateTime);
        localDateTime = LocalDateTime.now();
        System.out.println("localDateTime:"+localDateTime);
        ZonedDateTime zonedLocaDateTime= localDateTime.atZone(ZoneId.systemDefault());
        System.out.println("zonedLocaDateTime:"+zonedLocaDateTime+", "+ISO8601DateTimeFormatter.format(localDateTime));
        
        ZonedDateTime UTCDateTime = zonedLocaDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println("UTCDateTime:"+UTCDateTime+", "+UTC8601DateTimeFormatter.format(UTCDateTime));
    }
    
    /**
     * Une fois trouvé, on implémente la méthode dans la classe Event
     * et on teste le résultat.
     * ATTENTION : il faut modifier expUTCDateTime en fonction de l'heure d'été/hiver
     */
    @Test
    public void localDateTime2UTCDateTimeTest() {
        String localDateTime = "2023-03-07T12:00:00";
//        String expUTCDateTime = "2023-03-07T10:00:00Z"; // Heure d'été
        String expUTCDateTime = "2023-03-07T11:00:00Z"; // Heure d'hiver
        
        String UTCDateTime = a2vfclient.Event.localDateTime2UTCDateTime(localDateTime);
        
        System.out.println("localDateTime:"+localDateTime);
        System.out.println("UTCDateTime:"+UTCDateTime);
        assertEquals(expUTCDateTime, UTCDateTime);
    }
}
