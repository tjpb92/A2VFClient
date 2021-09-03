package a2vfclient;

import bdd.Fa2vf;
import bdd.Fa2vfDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ApplicationProperties;
import utils.DBManager;
import utils.DBServer;
import utils.DBServerException;
import utils.GetArgsException;

/**
 * Connecteur Anstel / Vinci Facilities (lien montant)
 *
 * @author Thierry Baribaud
 * @version 1.0.4
 */
public class A2VFClient {

    /**
     * Common Jackson object mapper
     */
//    private static final ObjectMapper objectMapper = new ObjectMapper();
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
     * @throws a2vfclient.HttpsClientException en cas de problème avec la
     * connexion Https.
     * @throws java.lang.ClassNotFoundException en cas de problème avec une
     * classe inconnue
     * @throws java.sql.SQLException en cas d'erreur d'entrée/sortie.
     * @throws a2vfclient.MailServer.MailServerException en cas de problème sur
     * l'envoi des mails.
     */
    public A2VFClient(String[] args) throws GetArgsException, IOException, APIREST.APIServerException, DBServerException, MailServer.MailServerException, ClassNotFoundException, SQLException {
        ApplicationProperties applicationProperties;
        DBServer ifxServer;
        DBServer mgoServer;
        DBManager informixDbManager;
        Connection informixConnection;

        System.out.println("Création d'une instance de A2VFClient ...");

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

//        System.out.println("Ouverture de la connexion avec le server API" + apiRest.getName() + " ...");
//        httpsClient = new HttpsClient(apiRest, debugMode);
//        System.out.println("Connexion avec le server API ouverte.");
        System.out.println("Ouverture de la connexion au serveur Informix : " + ifxServer.getName());
        informixDbManager = new DBManager(ifxServer);

        System.out.println("Connexion à la base de données : " + ifxServer.getDbName());
        informixConnection = informixDbManager.getConnection();

        System.out.println("Traitement des événements ...");
        processEvents(informixConnection);
    }

    /**
     * Traitement des événements
     */
    private void processEvents(Connection informixConnection) throws ClassNotFoundException {

        Fa2vf fa2vf;
        Fa2vfDAO fa2vfDAO;
        int i;
        String json;
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        dateFormat.setTimeZone(timeZone);
//        Event event;
//        OpenTicket openTicket;
        int retcode;
//        MongoCollection<Document> collection;
//        MongoCursor<Document> cursor;
        int nbClient;
//        BasicDBObject filter;
//        UpdateResult updateResult;
//        TicketOpened ticketOpened;
//        Client client;
//        TicketInfos ticketInfos;
//        String clientUuid;
//        String reference;
//        Patrimony patrimony;
//        CallPurpose callPurpose;
//        String callPurposeUuid;
//        TicketClosed ticketClosed;
//        CloseTicket closeTicket;
//        EventType eventType;
        int evtType;

//        collection = mongoDatabase.getCollection("clients");
//        System.out.println(collection.count() + " client(s) dans la base MongoDb");
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

// Simulate fake treatment, one error each 8 records
                if ((i % 8) != 0) {
                    retcode = 1;
                }

//                try {
//                    json = toJson(fa2vf, dateFormat);
//                    System.out.println("  json:" + json);
//                    event = objectMapper.readValue(json.toString(), Event.class);
//                    System.out.println("  " + event.getClass().getName() + ", " + event);
//
//                    if (event instanceof TicketOpened) {
//                        retcode = processTicketOpened(mongoDatabase, (TicketOpened) event, httpsClient);
//                    } else if (event instanceof InterventionStarted) {
//                        retcode = processInterventionStarted(mongoDatabase, (InterventionStarted) event, httpsClient);
//                    } else if (event instanceof InterventionFinished) {
//                        retcode = processInterventionFinished(mongoDatabase, (InterventionFinished) event, httpsClient);
//                    } else if (event instanceof PermanentlyFixed) {
//                        retcode = processPermanentlyFixed(mongoDatabase, (PermanentlyFixed) event, httpsClient);
//                    } else if (event instanceof ClosedQuoteRequested) {
//                        retcode = processClosedQuoteRequested(mongoDatabase, (ClosedQuoteRequested) event, httpsClient);
//                    } else if (event instanceof TicketClosed) {
//                        retcode = processTicketClosed(mongoDatabase, (TicketClosed) event, httpsClient);
//                    } else if (event instanceof TicketCancelled) {
//                        retcode = processTicketCancelled(mongoDatabase, (TicketCancelled) event, httpsClient);
//                    } else if (event instanceof TicketUpdated) {
//                        retcode = processTicketUpdated(mongoDatabase, (TicketUpdated) event, httpsClient);
//                    }
//                } catch (IOException exception) {
//                    retcode = -1;
//                    System.out.println("  Cannot convert Json to Event, record rejected " + exception);
////                        Logger.getLogger(A2ITClient.class.getName()).log(Level.SEVERE, null, exception);
//                } catch (UnknownEventTypeException exception) {
//                    retcode = -1;
//                    System.out.println(exception);
////                    Logger.getLogger(A2ITClient.class.getName()).log(Level.SEVERE, null, ex);
//                }
                fa2vf.setA12status(retcode);
                if (retcode != 1) {
                    fa2vf.setA12nberr(1);
                }

                fa2vf.setA12update(new Timestamp(new java.util.Date().getTime()));
                fa2vfDAO.update(fa2vf);
//                System.out.println("Rangée(s) affectée(s)=" + fa2vfDAO.getNbAffectedRow());
            }
            fa2vfDAO.closeUpdatePreparedStatement();
            fa2vfDAO.closeSelectPreparedStatement();

        } catch (SQLException exception) {
            Logger.getLogger(A2VFClient.class.getName()).log(Level.SEVERE, null, exception);
        }
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
                        if (args[ip1].equals("pre-prod") || args[ip1].equals("prod")
                                || args[ip1].equals("pre-prod2") || args[ip1].equals("prod2")) {
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
        } catch (GetArgsException | IOException | APIREST.APIServerException | DBServerException | MailServer.MailServerException | ClassNotFoundException | SQLException exception) {
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

}
