package a2vfclient;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Jeux de tests pour tester toute les classes du projet A2VFClient
 *
 * @author Thierry Baribaud
 * @version 1.21
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({LocalDateTime2UTCTest.class, localDateTime2UTCDateTimeTest.class, TicketInfosLocalDateTime2UTC.class})
public class A2VFClientSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

}
