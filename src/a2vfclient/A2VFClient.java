package a2vfclient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ApplicationProperties;
import utils.DBServer;
import utils.DBServerException;
import utils.GetArgsException;

/**
 * Connecteur Anstel / Vinci Facilities (lien montant)
 *
 * @author Thierry Baribaud
 * @version 1.0.3
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
    public A2VFClient(String[] args) throws GetArgsException, IOException, APIREST.APIServerException, DBServerException, MailServer.MailServerException {
        ApplicationProperties applicationProperties;
        DBServer ifxServer;
        DBServer mgoServer;

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
        } catch (GetArgsException | IOException | APIREST.APIServerException | DBServerException | MailServer.MailServerException exception) {
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