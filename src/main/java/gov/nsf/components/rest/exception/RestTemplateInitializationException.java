package gov.nsf.components.rest.exception;

/**
 * Exception class for errors during RestTemplate initialization
 */
public class RestTemplateInitializationException extends RuntimeException {

    public RestTemplateInitializationException(String message, Throwable cause) {
        super(message,cause);
    }
}
