package a2vfclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.HttpsClientException;

/**
 * Classe décrivant un client se connectant en HTTPS à un serveur
 *
 * @author Thierry Baribaud
 * @version 1.0.7
 */
public class HttpsClient extends OkHttpClient {

    /**
     * Common Jackson object mapper
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Paramètres du serveur
     */
    private APIREST apiRest;

    /**
     * Constructeur principal de la classe
     *
     * @param apiRest paramètres du serveur
     * @param debugMode indique si l'on est en mode debug ou non
     * @throws java.io.IOException en cas d'erreur de lecture du fichier des
     * propriétés
     * @throws HttpsClientException en cas d'erreur avec la connexion Https
     */
    public HttpsClient(APIREST apiRest, boolean debugMode) throws IOException, HttpsClientException {
        super();
        this.apiRest = apiRest;

//        StringBuffer mediaTypeParams;
//        MediaType mediaType;
//        String json;
//        int code;
//        String message;

//        mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        mediaTypeParams = new StringBuffer("grant_type=client_credentials&client_id=");
//        mediaTypeParams.append(this.apiRest.getLogin());
//        mediaTypeParams.append("&client_secret=");
//        mediaTypeParams.append(this.apiRest.getPassword());
//        if (debugMode) {
//            System.out.println("mediaTypeParams:" + mediaTypeParams.toString());
//        }
//        RequestBody body = RequestBody.create(mediaType, mediaTypeParams.toString());
//        Request request = new Request.Builder()
//                //                .url("https://accountsandbox.hubintent.com/oauth/token")
//                .url(apiRest.getAuthUrl())
//                .post(body)
//                .addHeader("content-type", "application/x-www-form-urlencoded")
//                .addHeader("cache-control", "no-cache")
//                .build();
//
//        Response response = this.newCall(request).execute();
////            System.out.println("response:" + response);
//        code = response.code();
//        message = response.message();
//
//        if (debugMode) {
//            System.out.println("response.code():" + code);
//            System.out.println("response.message():" + message);
//        }
//
//        if (code == 200) {
//            json = response.body().string();
//            System.out.println("response.body():" + json);
////            System.out.println("response.headers():" + response.headers());
//            if (json != null) {
//                token = objectMapper.readValue(json, Token.class);
//                System.out.println(token);
//            }
//        } else {
//            throw new HttpsClientException(code + " " + message);
//        }
    }

    /**
     * Créer une demande d'intervention sur la plate-forme
     *
     * @param ticketInfos commande d'ouverture de ticket
     * @param debugMode indique si l'on est en mode debug ou non
     * @throws com.fasterxml.jackson.core.JsonProcessingException en cas
     * d'erreur de convertion au format Json
     * @throws HttpsClientException en cas d'erreur avec la connexion
     * Https
     */
    public void openTicket(TicketInfos ticketInfos, boolean debugMode) throws JsonProcessingException, IOException, HttpsClientException {
        String url;
        String json;
        int code;
        String message;
        StringBuffer mediaTypeParams;

        url = this.apiRest.getBaseUrl() + "/ticket";
        if (debugMode) {
            System.out.println("  url:" + url);
        }

        objectMapper.writeValue(new File("testOpenTicket_2.json"), ticketInfos);
        json = objectMapper.writeValueAsString(ticketInfos);
        if (debugMode) {
            System.out.println("  openTicket:" + ticketInfos);
            System.out.println("  openTicket(json):" + json);
        }

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, json);
        if (debugMode) {
            System.out.println("  body.contentType():" + body.contentType() + ", body.contentLength():" + body.contentLength());
        }
        
        Request request = new Request.Builder()
                .url(url)
                .addHeader("client_id", apiRest.getLogin())
                .addHeader("client_secret", apiRest.getPassword())
                .addHeader("content-type", "application/json; charset=utf-8")
                .post(body)
                .build();
        if (debugMode) {
            System.out.println("  request.headers():" + request.headers());
        }
        
//        Response response = this.newCall(request).execute();
//        code = response.code();
//        message = response.message();

//        if (debugMode) {
//            System.out.println("  response.code():" + code);
//            System.out.println("  response.message():" + message);
//        }
//
//        if (code == 204) {
//            json = response.body().string();
//            System.out.println("    response.body():" + json);
//        } else {
//            throw new HttpsClientException(code + " " + message);
//        }

    }

}
