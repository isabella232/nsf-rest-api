package gov.nsf.components.rest.extractor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nsf.components.rest.exception.NsfClientException;
import gov.nsf.components.rest.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;


/**
 * ModelExtractor
 */
public class ModelExtractor<T> implements RestResponseExtractor<T> {

    private String rootName;
    private Class<T> classType;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ModelExtractor(Class classType) {
        this(classType, null);
    }

    public ModelExtractor(Class classType, String rootName) {
        Assert.notNull(classType, "<T> class type cannot be null");
        this.rootName = rootName;
        this.classType = classType;
    }

    /**
     * Extracts the type <T> from the JSON response payload
     *
     * If no "rootName" is passed, it assumes the Model is located at the root
     *
     * @param response - contains response data
     * @return
     */
    @Override
    public T extractResponse(ResponseEntity<String> response) {
        T model = null;

        JsonNode jsonResponseNode = null;
        try {
            jsonResponseNode = objectMapper.readTree(response.getBody());

            JsonNode modelNode = null;
            if (StringUtils.isBlank(rootName)) {
                modelNode = jsonResponseNode;
            } else {
                modelNode = jsonResponseNode.findValue(rootName);
            }

            if (modelNode == null) {
                model = null;
            } else {
                model = objectMapper.readValue(modelNode.toString(), classType);
            }
        } catch (JsonProcessingException e) {
            throw new NsfClientException(Constants.DESERIALIZATION_ERROR_MESSAGE, e, response.getBody(), null, response.getStatusCode().value());
        } catch (IOException e) {
            throw new NsfClientException(Constants.DESERIALIZATION_ERROR_MESSAGE, e, response.getBody(), null, response.getStatusCode().value());
        }

        return model;

    }

    @Override
    public T extractErrorResponse(HttpStatusCodeException ex) {
        throw ex;
    }

}
