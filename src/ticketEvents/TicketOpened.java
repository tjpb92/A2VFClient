package ticketEvents;

import a2vfclient.Event;
import a2vfclient.TicketInfos;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import static bkgpi2a.EventType.TICKET_OPENED;
import bkgpi2a.Operator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe décrivant l'événement ouverture de ticket : TicketOpened, #500
 *
 * @author Thierry Baribaud
 * @version 1.0.5
 */
@JsonIgnoreProperties({"_id", "date", "eventTypeUid"})
@JsonTypeName("TicketOpened")
public class TicketOpened extends Event {

    /**
     * Opérateur ayant ajouté le ticketInfos
     */
    private Operator operator;

    /**
     * Informations sur le ticket
     */
    @JsonProperty("ticket")
    private TicketInfos ticketInfos;

    /**
     * Constructeur de la classe TicketOpened
     */
    public TicketOpened() {
        setEventTypeUid(TICKET_OPENED.getUid());
        setEventType(TICKET_OPENED.getName());
    }

    /**
     * @return l'opérateur ayant ajouté le ticketInfos
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * @param operator définit l'opérateur ayant ajouté le ticketInfos
     */
    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    /**
     * @return les informations sur le ticket
     */
    public TicketInfos getTicketInfos() {
        return ticketInfos;
    }

    /**
     * @param ticketInfos définit les informations sur le ticket
     */
    public void setTicketInfos(TicketInfos ticketInfos) {
        this.ticketInfos = ticketInfos;
    }

    /**
     * @return la date à laquelle l'événement a eu lieu
     */
    public String getOpenedDate() {
        return getDate();
    }

    /**
     * @param openedDate définit la date à laquelle l'événement a eu lieu
     */
    public void setOpenedDate(String openedDate) {
        setDate(openedDate);
    }

    /**
     * @return Retourne l'objet sous forme textuelle
     */
    @Override
    public String toString() {
        return "TicketOpened:{"
                + super.toString()
                + ", " + getOperator()
                + ", " + getTicketInfos()
                + ", openedDate:" + getOpenedDate()
                + "}";
    }

}
