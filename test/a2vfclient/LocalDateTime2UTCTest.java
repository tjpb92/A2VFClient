package a2vfclient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Programme de tests pour le projet A2VFClient, conversion heure locale en UTC
 * phase 1
 *
 * @author Thierry Baribaud
 * @version 1.0.21
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
     * On recherche la méthode pour convertir le temps local en UTC ...
     */
    @Test
    public void localDateTime2UTCTest() {

        DateTimeFormatter UTC8601DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateTimeFormatter ISO8601DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        LocalDateTime localDateTime;
//        localDateTime = LocalDateTime.parse(localDateTime);
        localDateTime = LocalDateTime.now();
        System.out.println("localDateTime:" + localDateTime);
        ZonedDateTime zonedLocaDateTime = localDateTime.atZone(ZoneId.systemDefault());
        System.out.println("zonedLocaDateTime:" + zonedLocaDateTime + ", " + ISO8601DateTimeFormatter.format(localDateTime));

        ZonedDateTime UTCDateTime = zonedLocaDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println("UTCDateTime:" + UTCDateTime + ", " + UTC8601DateTimeFormatter.format(UTCDateTime));
    }

}
