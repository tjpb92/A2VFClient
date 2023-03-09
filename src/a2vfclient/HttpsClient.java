package a2vfclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * @version 1.0.19
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

    }

    /**
     * Créer une demande d'intervention sur la plate-forme
     *
     * @param ticketInfos commande d'ouverture de ticket
     * @param debugMode indique si l'on est en mode debug ou non
     * @return response du serveur API
     * @throws com.fasterxml.jackson.core.JsonProcessingException en cas
     * d'erreur de convertion au format Json
     */
    public APIResponse openTicket(TicketInfos ticketInfos, boolean debugMode) throws JsonProcessingException, IOException {
        String url;
        String json;
        Response response;
        APIResponse apiResponse = new APIResponse();

        url = this.apiRest.getBaseUrl() + "/tickets";
        if (debugMode) {
            System.out.println("  url:" + url);
        }

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

        response = this.newCall(request).execute();
        apiResponse.setCode(response.code());
        apiResponse.setMessage(response.message());
        apiResponse.setBody(response.body().string());

        if (debugMode) {
            System.out.println("  response.code():" + apiResponse.getCode());
            System.out.println("  response.message():" + apiResponse.getMessage());
            System.out.println("  response.body():" + apiResponse.getBody());
        }

        return apiResponse;

    }

}
