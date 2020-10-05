package gov.nsf.components.rest.extractor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nsf.components.rest.exception.NsfClientException;
import gov.nsf.components.rest.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * ListExtractor
 */
public class ListExtractor<T> implements RestResponseExtractor<ArrayList<T>> {

    private String rootName;
    private Class<T> classType;

    public ListExtractor(Class classType) {
        this(classType, null);
    }


    public ListExtractor(Class classType, String rootName) {
        this.rootName = rootName;
        this.classType = classType;
    }


    @Override
    public ArrayList<T> extractResponse(ResponseEntity<String> response) {
        ArrayList<T> collection = null;

        JsonNode jsonResponseNode = null;
        try {
            jsonResponseNode = getMapper().readTree(response.getBody());
            JsonNode collectionNode = null;
            if (StringUtils.isEmpty(rootName)) {
                collectionNode = jsonResponseNode;
            } else {
                collectionNode = jsonResponseNode.findValue(rootName);
            }

            if (collectionNode == null) {
                throw new NsfClientException(Constants.DESERIALIZATION_ERROR_MESSAGE, response.getBody(), null, response.getStatusCode().value());
            } else {
                collection = getMapper().readValue(collectionNode.toString(),
                        getMapper().getTypeFactory().constructCollectionType(ArrayList.class, classType));
            }
        } catch (JsonProcessingException e) {
            throw new NsfClientException(Constants.DESERIALIZATION_ERROR_MESSAGE, e, response.getBody(), null, response.getStatusCode().value());

        } catch (IOException e) {
            throw new NsfClientException(Constants.DESERIALIZATION_ERROR_MESSAGE, e, response.getBody(), null, response.getStatusCode().value());
        }

        return collection;

    }


    @Override
    public ArrayList<T> extractErrorResponse(HttpStatusCodeException ex) {
        throw ex;
    }

    public ObjectMapper getMapper() {
        return new ObjectMapper();
    }
}
