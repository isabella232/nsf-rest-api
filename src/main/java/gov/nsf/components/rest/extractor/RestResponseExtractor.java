package gov.nsf.components.rest.extractor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * Generic interface for extracting Java objects from JSON responses
 *
 * @param <T> - Type of object to be extracted
 * @author jlinden
 */
public interface RestResponseExtractor<T> {

    /**
     * Extract object from JSON response
     *
     * @param response - contains response data
     * @return Object
     */
    T extractResponse(ResponseEntity<String> response);

    /**
     * Extract object from JSON error response
     *
     * @param ex - contains response data
     * @return Object
     */
    T extractErrorResponse(HttpStatusCodeException ex);

}