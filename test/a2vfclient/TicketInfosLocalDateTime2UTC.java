package a2vfclient;

import bdd.Fa2vf;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ticketEvents.TicketOpened;
import utils.Md5;

/**
 * Programme de tests pour le projet A2VFClient, conversion heure locale en UTC
 * phase 3
 *
 * @author Thierry Baribaud
 * @version 1.0.21
 */
public class TicketInfosLocalDateTime2UTC {

    /**
     * Common Jackson object mapper
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public TicketInfosLocalDateTime2UTC() {
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
     * A présent, on exploite la méthode avec la classe TicketInfos. On part ici
     * avec un exemple réel venant d'Eole2. ATTENTION : peut être et doit être
     * mieux écrit !!!!
     */
    @Test
    public void ticketInfosLocalDateTime2UTC() {
        Fa2vf fa2vf = new Fa2vf();
        StringBuffer json;
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        dateFormat.setTimeZone(timeZone);
        Event event;
        TicketOpened ticketOpened;
        TicketInfos ticketInfos;

        fa2vf.setA12num(5369);
        fa2vf.setA12credate(Timestamp.valueOf("2023-03-06 14:59:08"));
        fa2vf.setA12onum0(1053);
        fa2vf.setA12laguid("c:5821053130306145646");
        fa2vf.setA12evttype(500);
        fa2vf.setA12data(
                "\"ticket\":{\"companyName\":\"LOOMA_F081_9544\",\"ticketOriginCod"
                + "e\":\"INTX\",\"contactEmailAddress\":\"no_email@looma.fr\",\"ticke"
                + "tSubject\":\"Fuite Radiateur Chauffage Collectif (Cassé ou n"
                + "on)\",\"ticketRemarks\":\"Fuite Rad Chauff Col/ fuite d'eau da]"
                + "ns la chaufferie, caller:mme bayarde, phone:06.08.49.01.68"
                + ", digicode:0, client:F127 TERRE AU CURE, address:43 rue de"
                + "s terres au cure, address2:f127, poscode:75015, city:PARIS"
                + ", bussinessCode:OUI\",\"problemTypeCode\":\"S-CVC\",\"ticketPrio"
                + "rity\":\"LOW\",\"ticketExternalId\":\"ANS_0582_044695\",\"siteId\":"
                + "\"9544/0000233\",\"siteExternalId\":\"9544/0000233\",\"ticketType"
                + "\":\"1\",\"ticketCreationDate\":\"2023-03-06T14:56:46\",\"dutyFlag"
                + "\":true,\"ticketBusinessStatus\":\"102\"},\"openedDate\":\"2023-03"
                + "-06T14:56:46\"");
        fa2vf.setA12status(0);
        fa2vf.setA12nberr(0);
        fa2vf.setA12size(0);
        fa2vf.setA12update(Timestamp.valueOf("2023-03-06 14:47:52"));
        System.out.println("fa2vf:" + fa2vf);

        // From A2VFClient.toJson(), difficult to use directly
        json = new StringBuffer("{");
        json.append("\"processUid\":\"").append(Md5.encode("a11:" + String.valueOf(fa2vf.getA12num()))).append("\",");
        json.append("\"aggregateUid\":\"").append(Md5.encode(fa2vf.getA12laguid())).append("\",");
//        json.append("\"eventType\":\"").append(eventType.getName()).append("\",");
        json.append("\"eventType\":\"").append("TicketOpened").append("\",");
        json.append("\"sentDate\":\"").append(dateFormat.format(fa2vf.getA12credate())).append("\",");
        json.append(fa2vf.getA12data());
        json.append("}");
        System.out.println("json:" + json);

        try {
            event = objectMapper.readValue(json.toString(), Event.class);
            System.out.println(event.getClass().getName() + ", " + event);

            ticketOpened = (TicketOpened) event;
            ticketInfos = ticketOpened.getTicketInfos();
            System.out.println("ticketInfos (temps local):" + ticketInfos);
            ticketInfos.convertLocalTime2UTC(); // Converstion temps local en temps UTC
            System.out.println("ticketInfos (temps UTC):" + ticketInfos);

            System.out.println("\nticketInfos.ticketCreationDate:" + ticketInfos.getTicketCreationDate());
            System.out.println("ticketOpened.openedDate" + ticketOpened.getOpenedDate());

        } catch (IOException iOException) {
            System.out.println("  Cannot convert Json to Event, record rejected " + iOException);
        }
    }
}
