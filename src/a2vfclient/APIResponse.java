package a2vfclient;

/**
 * Classe décrivant la réponse du serveur API
 *
 * @author Thierry Baribaud
 * @version 1.0.19 */

public class APIResponse {

    private int code;
    private String message;
    private String body;
    
    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "APIResponse{" + "code:" + code + ", message:" + message + ", body:" + body + '}';
    }
    
    
    
}
