/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a2vfclient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author thierry.baribaud
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
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
}
