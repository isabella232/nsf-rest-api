package gov.nsf.components.rest.authorization;


import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * HttpAuthorization interface
 *
 * @author jlinden
 */
public interface HttpAuthorization {
    /**
     * Returns the authorization headers for the given HttpAuthorization implementation
     *
     * @return Map of headers
     */
    MultiValueMap<String, ?> generateAuthorizationHeaders();
}