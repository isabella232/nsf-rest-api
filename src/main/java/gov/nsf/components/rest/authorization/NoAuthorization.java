package gov.nsf.components.rest.authorization;

import gov.nsf.components.rest.authorization.HttpAuthorization;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.Map;

/**
 * NoAuthorization is a safe null-object pattern for specifying that no authorization is required
 */
public class NoAuthorization implements HttpAuthorization {

    @Override
    public MultiValueMap<String, ?> generateAuthorizationHeaders() {
        return new HttpHeaders();
    }
}


