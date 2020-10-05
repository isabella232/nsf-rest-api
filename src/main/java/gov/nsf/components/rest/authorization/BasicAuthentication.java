package gov.nsf.components.rest.authorization;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

/**
 * BasicAuthentication implementation for HttpAuthorization
 */
public class BasicAuthentication implements HttpAuthorization {

    private String username; //Basic Auth "username"
    private String password; //Basic Auth "password"

    public BasicAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Generates Basic Authentication header in the format "Authorization : Basic [encoded credentials]"
     */
    @Override
    public MultiValueMap<String, ?> generateAuthorizationHeaders() {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + new String(encodedAuth));
        return headers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}