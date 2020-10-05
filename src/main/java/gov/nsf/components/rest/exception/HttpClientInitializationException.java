package gov.nsf.components.rest.exception;

/**
 * HttpClientInitializationException for exception that occur during HttpClient creation
 */
public class HttpClientInitializationException extends RestTemplateInitializationException {

    public HttpClientInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
