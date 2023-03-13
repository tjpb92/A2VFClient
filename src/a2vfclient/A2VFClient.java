package a2vfclient;

import bdd.Fa2vf;
import bdd.Fa2vfDAO;
import bkgpi2a.EventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import ticketEvents.TicketOpened;
import utils.ApplicationProperties;
import utils.DBManager;
import utils.DBServer;
import utils.DBServerException;
import utils.GetArgsException;
import utils.HttpsClientException;
import utils.Md5;
import utils.UnknownEventTypeException;

/**
 * Connecteur Anstel / Vinci Facilities (lien montant)
 *
 * @author Thierry Baribaud
 * @version 1.0.20
 */
public class A2VFClient {

    /**
     * Common Jackson object mapper
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * apiServerType : prod pour le serveur de production, pre-prod pour le
     * serveur de pré-production. Valeur par défaut : pre-prod.
     */
    private String apiServerType = "pre-prod";

    /**
     * Serveur de l'API REST
     */
    private APIREST apiRest;
    /**
     * mgoDbServerType : prod pour le serveur de production, pre-prod pour le
     * serveur de pré-production. Valeur par défaut : pre-prod.
     */
    private String mgoDbServerType = "pre-prod";

    /**
     * ifxDbServerType : prod pour le serveur de production, pre-prod pour le
     * serveur de pré-production. Valeur par défaut : pre-prod.
     */
    private String ifxDbServerType = "pre-prod";

    /**
     * Serveur de mail pour les notifications
     */
    private MailServer mailServer;

    /**
     * debugMode : fonctionnement du programme en mode debug (true/false).
     * Valeur par défaut : false.
     */
    private static boolean debugMode = false;

    /**
     * testMode : fonctionnement du programme en mode test (true/false). Valeur
     * par défaut : false.
     */
    private static boolean testMode = false;

    /**
     * EvenType lookup list by Uid
     */
    private Map<Integer, EventType> eventTypeByUid;

    /**
     * Constructeur de la classe A2VFClient
     *
     * @param args arguments en ligne de commande
     * @throws java.io.IOException en cas d'erreur d'entrée/sortie.
     * @throws utils.DBServerException en cas d'erreur avec le serveur de base
     * de données.
     * @throws utils.GetArgsException en cas d'erreur avec les paramètres en
     * ligne de commande
     * @throws a2vfclient.APIREST.APIServerException en cas de problème avec les
     * paramètres du serveur API
     * @throws utils.HttpsClientException en cas de problème avec la connexion
     * Https.
     * @throws java.lang.ClassNotFoundException en cas de problème avec une
     * classe inconnue
     * @throws java.sql.SQLException en cas d'erreur d'entrée/sortie.
     * @throws a2vfclient.MailServer.MailServerException en cas de problème sur
     * l'envoi des mails.
     */
    public A2VFClient(String[] args) throws GetArgsException, IOException, APIREST.APIServerException, DBServerException, MailServer.MailServerException, ClassNotFoundException, SQLException, HttpsClientException {
        ApplicationProperties applicationProperties;
        DBServer ifxServer;
        DBServer mgoServer;
        DBManager informixDbManager;
        Connection informixConnection;
        HttpsClient httpsClient;

        System.out.println("Création d'une instance de A2VFClient ...");

        System.out.println("Building EventType reverse Uid lookup list ...");
        eventTypeByUid = new HashMap<>();
        for (EventType eventType : EventType.values()) {
            eventTypeByUid.put(eventType.getUid(), eventType);
        }

        System.out.println("Analyse des arguments de la ligne de commande ...");
        this.getArgs(args);
        System.out.println("Argument(s) en ligne de commande lus().");

        System.out.println("Lecture des paramètres d'exécution ...");
        applicationProperties = new ApplicationProperties("A2VFClient.prop");
        System.out.println("Paramètres d'exécution lus.");

        System.out.println("Lecture des paramètres du serveur API ...");
        this.apiRest = new APIREST(apiServerType, applicationProperties);
        System.out.println("Paramètres du serveur API lus.");
        if (debugMode) {
            System.out.println(this.apiRest);
        }

        System.out.println("Lecture des paramètres du serveur Informix ...");
        ifxServer = new DBServer(ifxDbServerType, "ifxserver", applicationProperties);
        System.out.println("Paramètres du serveur Informix lus.");
        if (debugMode) {
            System.out.println(ifxServer);
        }

        System.out.println("Lecture des paramètres du serveur Mongo ...");
        mgoServer = new DBServer(mgoDbServerType, "mgoserver", applicationProperties);
        System.out.println("Paramètres du serveur Mongo lus.");
        if (debugMode) {
            System.out.println(mgoServer);
        }

        System.out.println("Lecture des paramètres du serveur de mail ...");
        mailServer = new MailServer(applicationProperties);
        System.out.println("Paramètres du serveur Mongo lus.");
        if (debugMode) {
            System.out.println(mailServer);
        }

        if (debugMode) {
            System.out.println(this.toString());
        }

        System.out.println("Ouverture de la connexion avec le server API" + apiRest.getName() + " ...");
        httpsClient = new HttpsClient(apiRest, debugMode);
        System.out.println("Connexion avec le server API ouverte.");

        System.out.println("Ouverture de la connexion au serveur Informix : " + ifxServer.getName());
        informixDbManager = new DBManager(ifxServer);

        System.out.println("Connexion à la base de données : " + ifxServer.getDbName());
        informixConnection = informixDbManager.getConnection();

        System.out.println("Traitement des événements ...");
        processEvents(httpsClient, informixConnection);
    }

    /**
     * Traitement des événements
     */
    private void processEvents(HttpsClient httpsClient, Connection informixConnection) throws ClassNotFoundException {

        Fa2vf fa2vf;
        Fa2vfDAO fa2vfDAO;
        int i;
        String json;
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        dateFormat.setTimeZone(timeZone);
        Event event;
        int retcode;
        int nbError;

        try {
            fa2vfDAO = new Fa2vfDAO(informixConnection);
            fa2vfDAO.setUpdatePreparedStatement();

            fa2vfDAO.filterByStatus(0);
            System.out.println("  SelectStatement=" + fa2vfDAO.getSelectStatement());
            fa2vfDAO.setSelectPreparedStatement();
            i = 0;

            while ((fa2vf = fa2vfDAO.select()) != null) {
                i++;
                retcode = -1;

                System.out.println("Fa2vf(" + i + ")=" + fa2vf);

                try {
                    json = toJson(fa2vf, dateFormat);
                    System.out.println("  json:" + json);
                    event = objectMapper.readValue(json, Event.class);
                    System.out.println("  " + event.getClass().getName() + ", " + event);

                    if (event instanceof TicketOpened) {
                        retcode = processTicketOpened((TicketOpened) event, httpsClient, fa2vf.getA12num(), fa2vf.getA12nberr());
                    }
//                    } else if (event instanceof InterventionStarted) {
//                        retcode = processInterventionStarted(mongoDatabase, (InterventionStarted) event, httpsClient);
//                    }
                } catch (IOException exception) {
                    retcode = -1;
                    System.out.println("  ERROR : Cannot convert Json to Event, record rejected " + exception);
                    sendAlert("LOOMA : ERROR - Cannot convert Json to Event, record rejected with fa2vf.a12num=" + fa2vf.getA12num(), exception.toString());
//                        Logger.getLogger(A2VFClient.class.getName()).log(Level.SEVERE, null, exception);
                } catch (UnknownEventTypeException exception) {
                    retcode = -1;
                    System.out.println(exception);
//                    Logger.getLogger(A2VFClient.class.getName()).log(Level.SEVERE, null, ex);
                }
//                fa2vf.setA12status(retcode);
                if (retcode == 1) {
                    fa2vf.setA12status(retcode);
                } else {
                    nbError = fa2vf.getA12nberr() + 1;
                    fa2vf.setA12nberr(nbError);
                    if (nbError >= 5) {
                        fa2vf.setA12status(-1);
                        sendAlert("LOOMA : " + nbError + " errors encountered with fa2vf.a12num=" + fa2vf.getA12num() + ", Record rejected");
                    }
                }

                fa2vf.setA12update(new Timestamp(new java.util.Date().getTime()));
                fa2vfDAO.update(fa2vf);
//                System.out.println("Rangée(s) affectée(s)=" + fa2vfDAO.getNbAffectedRow());

//                if (i >= 1) {
//                    break;
//                }
            }
            fa2vfDAO.closeUpdatePreparedStatement();
            fa2vfDAO.closeSelectPreparedStatement();

        } catch (SQLException exception) {
            Logger.getLogger(A2VFClient.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    /**
     * Traitement de l'ouverture d'un ticket
     *
     * @param ticketOpened événement d'ouverture d'un ticket
     * @param httpsClient connexion au serveur API
     *
     * @return résultat de l'opération 1=succès, 0=abandon, -1=erreur
     */
    private int processTicketOpened(TicketOpened ticketOpened, HttpsClient httpsClient, int a12num, int a12nbError) {
        TicketInfos ticketInfos;
        APIResponse apiResponse;
        int retcode;

        retcode = -1;
        ticketInfos = ticketOpened.getTicketInfos();

        ticketInfos.convertLocalTime2UTC(); // Converstion temps local en temps UTC

        try {
            apiResponse = httpsClient.openTicket(ticketInfos, debugMode);
            sendAlert(ticketInfos, a12num, a12nbError, apiResponse);
            if (apiResponse.getCode() == 200) {
                retcode = 1;
            }
        } catch (JsonProcessingException exception) {
            //                      Logger.getLogger(A2ITClient.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println("  ERROR : Fail to write Json to file " + exception);
            sendAlert("LOOMA : " + (a12nbError + 1) + " error(s) encountered, "
                    + " with ticket " + ticketInfos.getTicketExternalId()
                    + ", a12num:" + a12num, exception.toString());
        } catch (IOException exception) {
            System.out.println("  ERROR : fail to sent ticket to Looma " + exception);
            sendAlert("LOOMA : " + (a12nbError + 1) + " error(s) encountered, "
                    + " with ticket " + ticketInfos.getTicketExternalId()
                    + ", a12num:" + a12num, exception.toString());
            //                        Logger.getLogger(A2ITClient.class.getName()).log(Level.SEVERE, null, exception);
        }

        return retcode;
    }

    /**
     * Récupère les paramètres en ligne de commande
     *
     * @param args arguments en ligne de commande
     */
    private void getArgs(String[] args) throws GetArgsException {
        int i;
        int n;
        int ip1;

        n = args.length;
//        System.out.println("nargs=" + n);
//    for(i=0; i<n; i++) System.out.println("args["+i+"]="+Args[i]);
        i = 0;
        while (i < n) {
//            System.out.println("args[" + i + "]=" + Args[i]);
            ip1 = i + 1;
            switch (args[i]) {
                case "-apiserver":
                    if (ip1 < n) {
                        if (args[ip1].equals("pre-prod") || args[ip1].equals("prod")) {
                            this.apiServerType = args[ip1];
                        } else {
                            throw new GetArgsException("ERREUR : Mauvais serveur API : " + args[ip1]);
                        }
                        i = ip1;
                    } else {
                        throw new GetArgsException("ERREUR : Serveur API non défini");
                    }
                    break;
                case "-ifxserver":
                    if (ip1 < n) {
                        if (args[ip1].equals("pre-prod") || args[ip1].equals("prod")) {
                            this.ifxDbServerType = args[ip1];
                        } else {
                            throw new GetArgsException("ERREUR : Mauvais serveur Informix : " + args[ip1]);
                        }
                        i = ip1;
                    } else {
                        throw new GetArgsException("ERREUR : Serveur Informix non défini");
                    }
                    break;
                case "-mgoserver":
                    if (ip1 < n) {
                        if (args[ip1].equals("pre-prod") || args[ip1].equals("prod")) {
                            this.mgoDbServerType = args[ip1];
                        } else {
                            throw new GetArgsException("ERREUR : Mauvais serveur Mongo : " + args[ip1]);
                        }
                        i = ip1;
                    } else {
                        throw new GetArgsException("ERREUR : Serveur Mongo non définie");
                    }
                    break;
                case "-d":
                    setDebugMode(true);
                    break;
                case "-t":
                    setTestMode(true);
                    break;
                default:
                    usage();
                    throw new GetArgsException("ERREUR : Mauvais argument : " + args[i]);
            }
            i++;
        }
    }

    /**
     * @return retourne le serveur de mail
     */
//    public MailServer getMailServer() {
//        return mailServer;
//    }
    /**
     * @param mailServer définit le serveur de mail
     */
//    public void setMailServer(MailServer mailServer) {
//        this.mailServer = mailServer;
//    }
    /**
     * @param debugMode : fonctionnement du programme en mode debug
     * (true/false).
     */
    public void setDebugMode(boolean debugMode) {
        A2VFClient.debugMode = debugMode;
    }

    /**
     * @param testMode : fonctionnement du programme en mode test (true/false).
     */
    public void setTestMode(boolean testMode) {
        A2VFClient.testMode = testMode;
    }

    /**
     * @return debugMode : retourne le mode de fonctionnement debug.
     */
    public boolean getDebugMode() {
        return (debugMode);
    }

    /**
     * @return testMode : retourne le mode de fonctionnement test.
     */
    public boolean getTestMode() {
        return (testMode);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        A2VFClient a2VFClient;

        System.out.println("Lancement de A2VFclient ...");
        try {
            a2VFClient = new A2VFClient(args);
        } catch (GetArgsException | IOException | APIREST.APIServerException | DBServerException | MailServer.MailServerException | ClassNotFoundException | SQLException | HttpsClientException exception) {
            Logger.getLogger(A2VFClient.class.getName()).log(Level.SEVERE, null, exception);
        }
        System.out.println("Fin de A2VFclient.");
    }

    /**
     * Affiche le mode d'utilisation du programme.
     */
    public static void usage() {
        System.out.println("Usage : java A2VFclient [-apiserver prod|pre-prod]"
                + " [-ifxserver prod|pre-prod]"
                + " [-mgoserver prod|pre-prod]"
                + " [-d] [-t]");
    }

    /**
     * @param mgoDbServerType définit le serveur Web
     */
    private void setMgoDbServerType(String mgoDbServerType) {
        this.mgoDbServerType = mgoDbServerType;
    }

    /**
     * @param ifxDbServerType définit le serveur de base de données
     */
    private void setIfxDbServerType(String ifxDbServerType) {
        this.ifxDbServerType = ifxDbServerType;
    }

    /**
     * @return mgoDbServerType le serveur web
     */
    private String getMgoDbServerType() {
        return (mgoDbServerType);
    }

    /**
     * @return ifxDbServerType le serveur de base de données
     */
    private String getIfxDbServerType() {
        return (ifxDbServerType);
    }

    /**
     * Retourne le contenu de A2VFClient
     *
     * @return retourne le contenu de A2VFClient
     */
    @Override
    public String toString() {
        return "A2VFClient:{"
                + "apiServerType:" + apiServerType
                + ", ifxDbServerType:" + ifxDbServerType
                + ", mgoDbServerType:" + mgoDbServerType
                + ", mailServer:" + mailServer
                + ", debugMode:" + debugMode
                + ", testMode:" + testMode
                + "}";
    }

    /**
     * Conversion des données venant de la base de données Informix au foramt
     * Json
     *
     * @param fa2vf données à convertir
     * @param dateFormat format de date
     * @return données converties au format Json
     * @throws UnknownEventTypeException exception envoyée si un type
     * d'événément est inconnu
     */
    private String toJson(Fa2vf fa2vf, DateFormat dateFormat) throws UnknownEventTypeException {

        StringBuffer json;
        int evtType;
        EventType eventType;

        json = null;
        evtType = fa2vf.getA12evttype();
        eventType = eventTypeByUid.get(evtType);
        if (eventType == null) {
            throw new UnknownEventTypeException("Unknown EventType:" + evtType);
        } else {
            json = new StringBuffer("{");
            json.append("\"processUid\":\"").append(Md5.encode("a12:" + String.valueOf(fa2vf.getA12num()))).append("\",");
            json.append("\"aggregateUid\":\"").append(Md5.encode(fa2vf.getA12laguid())).append("\",");
            json.append("\"eventType\":\"").append(eventType.getName()).append("\",");
            json.append("\"sentDate\":\"").append(dateFormat.format(fa2vf.getA12credate())).append("\",");
            json.append(fa2vf.getA12data());
            json.append("}");
        }

        return json.toString();

    }

    /**
     * Envoie une alerte simple par mail
     */
    private void sendAlert(String alertMessage) {
        sendAlert(alertMessage, alertMessage);
    }

    /**
     * Envoie une alerte par mail pour un ticket ouvert
     *
     * @param ticketInfos commande d'ouverture de ticket
     * @param a12num identifiant unique de l'événement d'ouverture de ticket en
     * base de données.
     * @param a12nbError nombre d'erreur(s) actuel
     * @param response réponse du serveur API
     */
    public void sendAlert(TicketInfos ticketInfos, int a12num, int a12nbError, APIResponse response) {
        String alertSubject;
        StringBuffer alertMessage;
        String siteId;
        String ticketSubject;
        String ticketRemarks;
        String ticketCreationDate;
        String json;
        int code;
        String message;
        String body;

        code = response.getCode();
        switch (code) {
            case 200:
                alertSubject = "LOOMA : Ticket " + ticketInfos.getTicketExternalId() + " opened";
                break;
            default:
                alertSubject = "LOOMA : " + (a12nbError + 1) + " error(s) encountered, "
                        + " with ticket " + ticketInfos.getTicketExternalId()
                        + ", a12num:" + a12num;
        }

        alertMessage = new StringBuffer(alertSubject);
        if ((ticketSubject = ticketInfos.getTicketSubject()) != null) {
            alertMessage.append("\nObjet : ").append(ticketSubject);
        }
        if ((siteId = ticketInfos.getSiteId()) != null) {
            alertMessage.append("\nSite : ").append(siteId);
        }
        if ((ticketRemarks = ticketInfos.getTicketRemarks()) != null) {
            alertMessage.append("\nMotif : ").append("\n").append(ticketRemarks);
        }
        if ((ticketCreationDate = ticketInfos.getTicketCreationDate()) != null) {
            alertMessage.append("\nTicket saisi le : ").append("\n").append(ticketCreationDate).append(" UTC");
        }

        if (alertMessage.length() == 0) {
            alertMessage.append("\nATTENTION : \n\nchamps ticketSubject, siteId, ticketRemarks vides !");
        }
        
        alertMessage.append("\n\nDonnees envoyees à Looma :\n");
        try {
            json = objectMapper.writeValueAsString(ticketInfos);
            alertMessage.append(json);
        } catch (JsonProcessingException ex) {
            alertMessage.append("ERROR : cannot convert TicketInfos to Json, exeption:").append(ex);
            alertMessage.append(ticketInfos.toString());
//            Logger.getLogger(A2VFClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        message = response.getMessage();
        body = response.getBody();
        alertMessage.append("\n\nReponse du serveur API :");
        alertMessage.append("\n- code : ").append(code);
        alertMessage.append("\n- message : ").append(message);
        alertMessage.append("\n- body : ").append(body);

        sendAlert(alertSubject, alertMessage.toString());
    }

    /**
     * Envoie une alerte par mail
     *
     * @param alertSubject nom de l'alerte
     * @param alertMessage contenu de l'alerte
     */
    public void sendAlert(String alertSubject, String alertMessage) {
        try {
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", mailServer.getIpAddress());
            Session session = Session.getDefaultInstance(properties, null);
            javax.mail.Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailServer.getFromAddress()));
            InternetAddress[] internetAddresses = new InternetAddress[1];
            internetAddresses[0] = new InternetAddress(mailServer.getToAddress());
            message.setRecipients(javax.mail.Message.RecipientType.TO, internetAddresses);
            message.setSubject(alertSubject);
            message.setText(alertMessage);
            message.setHeader("X-Mailer", "Java");
            message.setSentDate(new Date());
            session.setDebug(debugMode);
            Transport.send(message);
        } catch (AddressException exception) {
            System.out.println("Problème avec une adresse mail " + exception);
        } catch (MessagingException exception) {
            System.out.println("Problème avec les mails " + exception);
        }

    }

}
