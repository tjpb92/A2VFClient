package a2vfclient;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Programme de tests pour le projet A2VFClient, conversion heure locale en UTC
 * phase 2
 *
 * @author Thierry Baribaud
 * @version 1.0.21
 */
public class localDateTime2UTCDateTimeTest {

    public localDateTime2UTCDateTimeTest() {
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
     * Une fois trouvé, on implémente la méthode dans la classe Event et on
     * teste le résultat. ATTENTION : il faut modifier expUTCDateTime en
     * fonction de l'heure d'été/hiver
     */
    @Test
    public void localDateTime2UTCDateTimeTest() {
        String localDateTime = "2023-03-07T12:00:00";
//        String expUTCDateTime = "2023-03-07T10:00:00Z"; // Heure d'été
        String expUTCDateTime = "2023-03-07T11:00:00Z"; // Heure d'hiver

        String UTCDateTime = a2vfclient.Event.localDateTime2UTCDateTime(localDateTime);

        System.out.println("localDateTime:" + localDateTime);
        System.out.println("UTCDateTime:" + UTCDateTime);
        assertEquals(expUTCDateTime, UTCDateTime);
    }
}
