package gov.nsf.components.rest.extractor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import gov.nsf.components.rest.model.message.NsfMessageCode;
import gov.nsf.components.rest.model.message.NsfResponseMessage;
import gov.nsf.components.rest.model.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;

/**
 * BaseResponseExtractor
 */
public class BaseResponseExtractor extends JsonResponseExtractor implements RestResponseExtractor<BaseResponse>{

    public BaseResponseExtractor(boolean sideLoaded) {
        super(sideLoaded);
    }

    @Override
    public BaseResponse extractResponse(ResponseEntity<String> response) {
        return getBaseResponse(response.getBody(), response.getStatusCode().value());
    }

    @Override
    public BaseResponse extractErrorResponse(HttpStatusCodeException ex) {
        return getBaseResponse(ex.getResponseBodyAsString(), ex.getStatusCode().value());
    }

    private BaseResponse getBaseResponse( String body, int httpCode ){
        BaseResponse baseResponse = new BaseResponse();

        JsonNode jsonResponseNode = null;
        try {
            jsonResponseNode = getMapper().readTree(body);
            JsonNode baseResponseNode = null;

            if (isSideLoaded()) {
                baseResponseNode = jsonResponseNode;
            } else {
                baseResponseNode = jsonResponseNode.findValue("baseResponse");
            }

            if (baseResponseNode == null) {
                baseResponse.addError(new NsfResponseMessage(null,
                        NsfMessageCode.JSON_PARSING_ERROR.getValue(),
                        "Expected baseResponse object but was " + body,
                        httpCode,
                        NsfMessageCode.JSON_PARSING_ERROR,
                        null,
                        null));
            } else {
                baseResponse = getMapper().readValue(baseResponseNode.toString(), BaseResponse.class);
            }
        } catch (JsonProcessingException e) {
            baseResponse.addError(new NsfResponseMessage(null,
                    NsfMessageCode.JSON_PARSING_ERROR.getValue(),
                    "Expected baseResponse object but was " + body,
                    httpCode,
                    NsfMessageCode.JSON_PARSING_ERROR,
                    null,
                    null)); // Wrapper not found
        } catch (IOException e) {
            baseResponse.addError(new NsfResponseMessage(null,
                    NsfMessageCode.CLIENT_NETWORK_ERROR.getValue(),
                    "Client network error occurred: " + e.getMessage(),
                    httpCode,
                    NsfMessageCode.CLIENT_NETWORK_ERROR,
                    null,
                    null));
        }

        return baseResponse;
    }
}
