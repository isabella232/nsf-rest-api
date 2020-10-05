package gov.nsf.components.rest.model.message;

@Deprecated
public enum NsfMessageCode {


    JSON_PARSING_ERROR("Json Parsing Error"),
    CLIENT_NETWORK_ERROR("Client Network Error"),
    INVALID_ID("Invalid ID");

    private String value;

    NsfMessageCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
