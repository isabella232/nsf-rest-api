package gov.nsf.components.rest.model.patch;

/**
 * PatchException
 */

public class PatchException extends Exception {

    public PatchException(String message) {
        super(message);
    }

    public PatchException(String message, Exception ex) {
        super(message, ex);
    }
}
