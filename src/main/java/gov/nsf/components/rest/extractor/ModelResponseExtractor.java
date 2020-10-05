package gov.nsf.components.rest.extractor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nsf.components.rest.exception.NsfClientException;
import gov.nsf.components.rest.model.message.NsfResponseMessage;
import gov.nsf.components.rest.model.response.ModelResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic Response Extractor class for ModelResponse's
 *
 * @author jlinden
 */
public class ModelResponseExtractor<T> implements RestResponseExtractor<ModelResponse<T>> {

    private final Class<T> genericType;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonPathInfo jsonPathInfo;


    protected static final String DESERIALIZATION_ERROR_MESSAGE = "An error occurred de-serializing the Response";

    /**
     * Default constructor that extracts the following format:
     *
     * {
     *    "data" : "This is my data of type String.class",
     *    "errors" : [],
     *    "warnings" : [],
     *    "informationals" : []
     * }
     *
     * new ModelResponseExtractorV2(String.class)
     * Assumes there is no root name and the data field is named "data"
     * @param type
     */
    public ModelResponseExtractor(Class<T> type) {
        this(type, new JsonPathInfo(null, null));
    }

    /**
     * Allows you to specify the root of the ModelResponse object AND the custom name of the "data" field
     *
     * E.g. new ModelResponseExtractorV2(String.class, new JsonPathInfo("modelResponse", "stringData"));

     * {
     *     "modelResponse" : {
     *         "stringData" : "This is my data of type String.class",
     *         "errors" : [],
     *         "warnings" : [],
     *         "informationals : []
     *     }
     * }
     *  E.g. new ModelResponseExtractorV2(String.class, new JsonPathInfo(null, "stringData"));
     *
     * {
     *
     *      "stringData" : "This is my data of type String.class",
     *      "errors" : [],
     *      "warnings" : [],
     *      "informationals : []
     *
     * }
     *  E.g. new ModelResponseExtractorV2(String.class, new JsonPathInfo("modelResponse", null));
     * {
     *
     *     "modelResponse" : {
     *         "data" : "This is my data of type String.class",
     *         "errors" : [],
     *         "warnings" : [],
     *         "informationals : []
     *     }
     * }
     *
     * @param type - class type of the "data" field
     * @param jsonPathInfo - json path info
     */
    public ModelResponseExtractor(Class<T> type, JsonPathInfo jsonPathInfo) {
        Assert.notNull(type, "<T> class type cannot be null");

        this.genericType = type;
        this.jsonPathInfo = jsonPathInfo == null ? new JsonPathInfo() : jsonPathInfo;
    }

    /**
     * Extracts the parameterized ModelResponse object from the JSON response
     */
    @Override
    public ModelResponse<T> extractResponse(ResponseEntity<String> response) {
        ModelResponse<T> modelResponse = null;

        try {
            modelResponse = extract(response.getBody());
        } catch (Exception ex) {
            throw new NsfClientException(DESERIALIZATION_ERROR_MESSAGE, ex, response.getBody(), null, response.getStatusCode().value());
        }

        return modelResponse;
    }

    /**
     * Extracts the parameterized ModelResponse object from the error response
     */
    @SuppressWarnings("unchecked")
    @Override
    public ModelResponse<T> extractErrorResponse(HttpStatusCodeException ex) {

        ModelResponse<T> response = null;

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

    public ModelResponse<T> extract(String body) throws IOException {
        ModelResponse<T> modelResponse = new ModelResponse<T>();

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
                    modelResponse.setErrors((List<NsfResponseMessage>) objectMapper.readValue(errors.toString(), objectMapper.getTypeFactory().constructParametricType(ArrayList.class, NsfResponseMessage.class)));
                }

                if (warnings != null) {
                    modelResponse.setWarnings((List<NsfResponseMessage>) objectMapper.readValue(warnings.toString(), objectMapper.getTypeFactory().constructParametricType(ArrayList.class, NsfResponseMessage.class)));
                }

                if (informationals != null) {
                    modelResponse.setInformationals((List<NsfResponseMessage>) objectMapper.readValue(informationals.toString(), objectMapper.getTypeFactory().constructParametricType(ArrayList.class, NsfResponseMessage.class)));
                }

                if (data != null) {
                    modelResponse.setData(objectMapper.readValue(data.toString(), genericType));
                }
            }
        } catch (IOException ioEx) {
            throw ioEx;
        }

        return modelResponse;
    }

    private JsonNode getResponseNode(String body) throws IOException {
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