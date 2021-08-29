package a2vfclient;

import utils.ApplicationProperties;

/**
 * Classe décrivant les paramètres d'accès à une API REST.
 *
 * @author Thierry Baribaud
 * @version 1.0.2
 */
public class APIREST {

    /**
     * Nom du serveur de l'API REST
     */
    private String name;
    
    /** 
     * URL de base 
     */
    private String baseUrl;
    
    /**
     * Authentification URL
     */
    private String authUrl;

    /**
     * Identifiant de l'API
     */
    private String login;
    
    /**
     * Mot de passe de l'API
     */
    private String password;

    /**
     * Exception pouvant être lancée en cas de mauvais paramètres dans le fichier
     * des propriétés
     */
    public class APIServerException extends Exception {

	private final static String ERRMSG = 
		"Problem during instantiation of a APIServer object";

	public APIServerException() {
		super(ERRMSG);
	}

	public APIServerException(String errMsg) {
		super(ERRMSG + " : " + errMsg);
	}
    }
    
    /**
     * Constructeur principal de la classe APIREST
     * @param apiServerType type de serveur d'API : (prod, preprod, ...)
     * @param applicationProperties définit les propiétés de l'API REST depuis un fichier de propriétés
     * @throws a2vfclient.APIREST.APIServerException exception lancé en cas de problème
     */
    public APIREST(String apiServerType, ApplicationProperties applicationProperties) throws APIServerException {
        String value;

        value = applicationProperties.getProperty(apiServerType + ".apiserver.name");
        if (value != null) {
            this.name = value;
        } else {
            throw new APIServerException("Le nom du serveur API n'est pas défini");
        }

        value = applicationProperties.getProperty(apiServerType + ".apiserver.baseURL");
        if (value != null) {
            this.baseUrl = value;
        } else {
            throw new APIServerException("L'URL de base de l'API n'est pas définie");
        }

        value = applicationProperties.getProperty(apiServerType + ".apiserver.authURL");
        if (value != null) {
            this.authUrl = value;
        } else {
            throw new APIServerException("L'URL d'authentification de l'API n'est pas définie");
        }

        value = applicationProperties.getProperty(apiServerType + ".apiserver.login");
        if (value != null) {
            this.login = value;
        } else {
            throw new APIServerException("L'identifiant de l'API n'est pas défini");
        }

        value = applicationProperties.getProperty(apiServerType + ".apiserver.passwd");
        if (value != null) {
            this.password = value;
        } else {
            throw new APIServerException("Le mot de passe de l'API n'est pas défini");
        }
    }
    
    /**
     * Retourne le nom du serveur de l'API REST
     *
     * @return le nom du serveur de l'API REST
     */
    public String getName() {
        return (name);
    }

    /**
     * Définit le nom du serveur de l'API REST
     *
     * @param name définit le nom du serveur de l'API REST
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return retourne l'URL de base
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * @param baseUrl définit l'URL de base
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * @return retourne l'URL d'authentification
     */
    public String getAuthUrl() {
        return authUrl;
    }

    /**
     * @param authUrl définit l'URL d'authentification
     */
    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    /**
     * @return l'identifiant de l'API.
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login définit l'identifant de l'API.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return le mot de passe de l'API
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password définit le mot de passe de l'API
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retourne les valeurs sous forme textuelle
     *
     * @return Retourne les valeurs sous forme textuelle
     */
    @Override
    public String toString() {
        return "APIREST:{"
                + "name:" + name
                + ", baseUrl:" + baseUrl
                + ", authUrl:" + authUrl
                + ", login:" + login
                + ", password:" + password
                + "}";
    }

}
