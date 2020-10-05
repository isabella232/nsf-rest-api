package gov.nsf.components.rest.extractor;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nsf.components.rest.exception.NsfClientException;
import gov.nsf.components.rest.model.message.NsfResponseMessage;
import gov.nsf.components.rest.model.response.CollectionResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Generic Response Extractor class for CollectionResponse's
 *
 * @author jlinden
 */
public class CollectionResponseExtractor<T> implements RestResponseExtractor<CollectionResponse<T>> {

    private final Class<T> genericType;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonPathInfo jsonPathInfo;


    protected static final String DESERIALIZATION_ERROR_MESSAGE = "An error occurred de-serializing the Response";

    /**
     * Default constructor that extracts the following format:
     * 
     * {
     * "data" : "This is my data of type String.class",
     * "errors" : [],
     * "warnings" : [],
     * "informationals" : []
     * }
     * 
     * new CollectionResponseExtractorV2(String.class)
     * Assumes there is no root name and the data field is named "data"
     *
     * @param type
     */
    public CollectionResponseExtractor(Class<T> type) {
        this(type, new JsonPathInfo(null, null));
    }

    /**
     * Allows you to specify the root of the CollectionResponse object AND the custom name of the "data" field
     * 
     * E.g. new CollectionResponseExtractorV2(String.class, new JsonPathInfo("CollectionResponse", "stringData"));
     * 
     * {
     * "CollectionResponse" : {
     * "stringData" : "This is my data of type String.class",
     * "errors" : [],
     * "warnings" : [],
     * "informationals : []
     * }
     * }
     * E.g. new CollectionResponseExtractorV2(String.class, new JsonPathInfo(null, "stringData"));
     * 
     * {
     * 
     * "stringData" : "This is my data of type String.class",
     * "errors" : [],
     * "warnings" : [],
     * "informationals : []
     * 
     * }
     * E.g. new CollectionResponseExtractorV2(String.class, new JsonPathInfo("CollectionResponse", null));
     * {
     * 
     * "CollectionResponse" : {
     * "data" : "This is my data of type String.class",
     * "errors" : [],
     * "warnings" : [],
     * "informationals : []
     * }
     * }
     *
     * @param type         - class type of the "data" field
     * @param jsonPathInfo - json path info
     */
    public CollectionResponseExtractor(Class<T> type, JsonPathInfo jsonPathInfo) {
        this.genericType = type;
        this.jsonPathInfo = jsonPathInfo == null ? new JsonPathInfo() : jsonPathInfo;
    }

    /**
     * Extracts the parameterized CollectionResponse object from the JSON response
     */
    @Override
    public CollectionResponse<T> extractResponse(ResponseEntity<String> response) {
        CollectionResponse<T> CollectionResponse = null;

        try {
            CollectionResponse = extract(response.getBody());
        } catch (Exception ex) {
            throw new NsfClientException(DESERIALIZATION_ERROR_MESSAGE, ex, response.getBody(), null, response.getStatusCode().value());
        }

        return CollectionResponse;
    }

    /**
     * Extracts the parameterized CollectionResponse object from the error response
     */
    @SuppressWarnings("unchecked")
    @Override
    public CollectionResponse<T> extractErrorResponse(HttpStatusCodeException ex) {

        CollectionResponse<T> response = null;

        try {
            response = extract(ex.getResponseBodyAsString());
        } catch (Exception e) {
            throw new NsfClientException(DESERIALIZATION_ERROR_MESSAGE, e, ex.getResponseBodyAsString(), null, ex.getStatusCode().value());
        }

        if (response.isEmpty()) {
            throw ex;
        }

        return response;
    }

    public CollectionResponse<T> extract(String body) throws IOException {
        CollectionResponse<T> collectionResponse = new CollectionResponse<T>();

        JsonNode jsonResponseNode = null;
        JsonNode errors = null;
        JsonNode warnings = null;
        JsonNode informationals = null;
        JsonNode data = null;

        try {
            jsonResponseNode = getResponseNode(body);

            if (jsonResponseNode != null) {

                errors = jsonResponseNode.findValue("errors");
                warnings = jsonResponseNode.findValue("warnings");
                informationals = jsonResponseNode.findValue("informationals");
                data = getDataNode(jsonResponseNode);

                if (errors != null) {
                    collectionResponse.setErrors((List<NsfResponseMessage>) objectMapper.readValue(errors.toString(), objectMapper.getTypeFactory().constructParametricType(ArrayList.class, NsfResponseMessage.class)));
                }

                if (warnings != null) {
                    collectionResponse.setWarnings((List<NsfResponseMessage>) objectMapper.readValue(warnings.toString(), objectMapper.getTypeFactory().constructParametricType(ArrayList.class, NsfResponseMessage.class)));
                }

                if (informationals != null) {
                    collectionResponse.setInformationals((List<NsfResponseMessage>) objectMapper.readValue(informationals.toString(), objectMapper.getTypeFactory().constructParametricType(ArrayList.class, NsfResponseMessage.class)));
                }

                if (data != null) {
                    collectionResponse.setData((Collection<T>) objectMapper.readValue(data.toString(), objectMapper.getTypeFactory().constructParametricType(ArrayList.class, genericType)));
                }
            }
        } catch (IOException ioEx) {
            throw ioEx;
        }

        return collectionResponse;
    }

    private JsonNode getResponseNode(String body)  {

        JsonNode responseNode = null;
        try {
            responseNode = objectMapper.readTree(body);
        } catch (IOException e) {
            return null;
        }

        if (StringUtils.isNotBlank(this.jsonPathInfo.getRootName())) {
            responseNode = responseNode.findValue(this.jsonPathInfo.getRootName());
        }

        return responseNode;
    }

    private JsonNode getDataNode(JsonNode responseNode) throws IOException {
        JsonNode dataNode = responseNode.findValue("data");
        if (StringUtils.isNotBlank(this.jsonPathInfo.getDataName())) {
            dataNode = responseNode.findValue(this.jsonPathInfo.getDataName());
        }

        return dataNode;
    }

}