package gov.nsf.components.rest.exception;

import gov.nsf.components.rest.model.message.NsfResponseMessage;

import java.util.List;

/**
 * NsfClientException
 */
public class NsfClientException extends RuntimeException {

    private String responseBody;
    private List<NsfResponseMessage> errors;
    private int httpStatusCode;

    public NsfClientException(String responseBody, List<NsfResponseMessage> errors, int httpStatusCode) {
        this.responseBody = responseBody;
        this.errors = errors;
        this.httpStatusCode = httpStatusCode;
    }

    public NsfClientException(String message, String responseBody, List<NsfResponseMessage> errors, int httpStatusCode) {
        super(message);
        this.responseBody = responseBody;
        this.errors = errors;
        this.httpStatusCode = httpStatusCode;
    }

    public NsfClientException(String message, Throwable cause, String responseBody, List<NsfResponseMessage> errors, int httpStatusCode) {
        super(message, cause);
        this.responseBody = responseBody;
        this.errors = errors;
        this.httpStatusCode = httpStatusCode;
    }

    public NsfClientException(Throwable cause, String responseBody, List<NsfResponseMessage> errors, int httpStatusCode) {
        super(cause);
        this.responseBody = responseBody;
        this.errors = errors;
        this.httpStatusCode = httpStatusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public List<NsfResponseMessage> getErrors() {
        return errors;
    }

    public void setErrors(List<NsfResponseMessage> errors) {
        this.errors = errors;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
}
