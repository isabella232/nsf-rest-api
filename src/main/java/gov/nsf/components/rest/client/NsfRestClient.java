package gov.nsf.components.rest.client;

import gov.nsf.components.rest.authorization.HttpAuthorization;
import gov.nsf.components.rest.authorization.NoAuthorization;
import gov.nsf.components.rest.extractor.RestResponseExtractor;
import gov.nsf.components.rest.template.RestTemplateBuilder;
import gov.nsf.components.rest.util.NsfRestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NsfRestClient super class for NSF service REST clients
 * <p>
 * Service clients should extend this class for its executeRequest method and
 * its authorization/timeout fields
 *
 * @author jlinden
 */
public abstract class NsfRestClient {

    private final RestTemplate restTemplate;
    private final HttpAuthorization authorization;

    public NsfRestClient() {
        this(new NoAuthorization());
    }

    public NsfRestClient(HttpAuthorization authorization) {
        this(RestTemplateBuilder.defaultRestTemplate(), authorization);
    }

    public NsfRestClient(RestTemplate restTemplate, HttpAuthorization authorization) {
        this.restTemplate = restTemplate;
        this.authorization = authorization;
    }

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    /**
     * @param url           - the full service url and controller endpoint with url
     *                      parameters appended
     * @param httpMethod    - the HTTP request method
     * @param requestBody   - the JSON request body
     * @param uriParameters - the URI parameters (e.g., path variables)
     * @param responseType  - the response Java class type
     * @return object of the responseType class
     */
    public <T> T executeForObject(String url, HttpMethod httpMethod, MultiValueMap<String, ?> headers,
                                  String requestBody, Map<String, ?> uriParameters, Class<T> responseType) {

        HttpEntity<String> httpEntity = null;
        if (authorization == null || authorization instanceof NoAuthorization) {
            httpEntity = NsfRestUtils.createHttpEntity(requestBody, headers != null ? headers : null);
        } else {
            httpEntity = NsfRestUtils.createHttpEntity(requestBody, headers != null ? headers : null, authorization);
        }

        logRequest(url, httpMethod, headers, requestBody, uriParameters);

        ResponseEntity<T> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, responseType, uriParameters);
            logResponse(responseEntity);
        } catch (HttpStatusCodeException ex) {
            logErrorResponse(ex);
            throw ex;
        }

        return responseEntity.getBody();
    }


    /**
     * @param url          - the full service url and controller endpoint with url
     *                     parameters appended
     * @param headers      - additional headers
     * @param httpMethod   - the HTTP request method
     * @param requestBody  - the JSON request body
     * @param responseType - the response Java class type
     * @return object of the responseType class
     */
    public <T> T executeForObject(String url, HttpMethod httpMethod, MultiValueMap<String, ?> headers,
                                  String requestBody, Class<T> responseType) {

        return executeForObject(url, httpMethod, headers, requestBody, new HashMap<String, String>(), responseType);
    }


    /**
     * @param url           - the full service url and controller endpoint with url
     *                      parameters appended
     * @param httpMethod    - the HTTP request method
     * @param headers       - the additional headers
     * @param uriParameters - the URI parameters (e.g., path variables)
     * @param responseType  - the response Java class type
     * @return object of the responseType class
     */
    public <T> T executeForObject(String url, HttpMethod httpMethod, MultiValueMap<String, ?> headers, Map<String, ?> uriParameters,
                                  Class<T> responseType) {

        return executeForObject(url, httpMethod, headers, null, uriParameters, responseType);
    }

    /**
     * @param url          - the full service url and controller endpoint with url
     *                     parameters appended
     * @param httpMethod   - the HTTP request method
     * @param headers      - the additional headers
     * @param responseType - the response Java class type
     * @return object of the responseType class
     */
    public <T> T executeForObject(String url, HttpMethod httpMethod, MultiValueMap<String, ?> headers,
                                  Class<T> responseType) {

        return executeForObject(url, httpMethod, headers, null, new HashMap<String, String>(), responseType);
    }

    /**
     * @param url           - the full service url and controller endpoint with url
     *                      parameters appended
     * @param httpMethod    - the HTTP request method
     * @param uriParameters - the uri Parameters
     * @param responseType  - the response Java class type
     * @return object of the responseType class
     */
    public <T> T executeForObject(String url, HttpMethod httpMethod, Map<String, ?> uriParameters,
                                  Class<T> responseType) {

        return executeForObject(url, httpMethod, new HttpHeaders(), null, uriParameters, responseType);
    }

    /**
     * @param url          - the full service url and controller endpoint with url
     *                     parameters appended
     * @param httpMethod   - the HTTP request method
     * @param requestBody  - the JSON request body
     * @param responseType - the response Java class type
     * @return object of the responseType class
     */
    public <T> T executeForObject(String url, HttpMethod httpMethod, String requestBody,
                                  Class<T> responseType) {

        return executeForObject(url, httpMethod, new HttpHeaders(), requestBody, new HashMap<String, String>(), responseType);
    }

    /**
     * @param url          - the full service url and controller endpoint with url
     *                     parameters appended
     * @param httpMethod   - the HTTP request method
     * @param responseType - the response Java class type
     * @return object of the responseType class
     */
    public <T> T executeForObject(String url, HttpMethod httpMethod,
                                  Class<T> responseType) {
        return executeForObject(url, httpMethod, new HttpHeaders(), null, new HashMap<String, String>(), responseType);
    }


    /**
     * @param url               - the full service url and controller endpoint with url
     *                          parameters appended
     * @param httpMethod        - the HTTP request method
     * @param requestBody       - the JSON request body
     * @param uriParameters     - the URI parameters (e.g., path variables)
     * @param responseExtractor - the JSON response extractor for retrieving Java objects from
     *                          the response
     * @return object of the Type the responseExtractor retrieves
     */
    public <T> T executeRequest(String url, HttpMethod httpMethod, MultiValueMap<String, ?> headers, String requestBody, Map<String, ?> uriParameters,
                                RestResponseExtractor<T> responseExtractor) {


        HttpEntity<String> httpEntity = null;
        if (authorization == null || authorization instanceof NoAuthorization) {
            httpEntity = NsfRestUtils.createHttpEntity(requestBody, headers != null ? headers : null);
        } else {
            httpEntity = NsfRestUtils.createHttpEntity(requestBody, headers != null ? headers : null, authorization);
        }

        logRequest(url, httpMethod, headers, requestBody, uriParameters);

        ResponseEntity<String> responseEntity = null;
        T response = null;
        try {
            responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class, uriParameters);
            logResponse(responseEntity);
            response = responseExtractor.extractResponse(responseEntity);
        } catch (HttpStatusCodeException ex) {
            logErrorResponse(ex);
            response = responseExtractor.extractErrorResponse(ex);
        }


        return response;
    }

    /**
     * @param url               - the full service url and controller endpoint with url
     *                          parameters appended
     * @param httpMethod        - the HTTP request method
     * @param entity            - the HTTP entity
     * @param uriParameters     - the URI parameters (e.g., path variables)
     * @param responseExtractor - the JSON response extractor for retrieving Java objects from
     *                          the response
     * @return object of the Type the responseExtractor retrieves
     */
    public <T> T exchange(String url, HttpMethod httpMethod, HttpEntity<?> entity, Map<String, ?> uriParameters,
                          RestResponseExtractor<T> responseExtractor) {

        HttpHeaders httpHeaders = copyHeaders(entity.getHeaders());
        Object body = entity.getBody();

        if (authorization != null && !(authorization instanceof NoAuthorization)) {
            httpHeaders.putAll((MultiValueMap<String, String>) authorization.generateAuthorizationHeaders());
        }

        logRequest(url, httpMethod, httpHeaders, null, uriParameters);

        ResponseEntity<String> responseEntity = null;
        T response = null;
        try {
            responseEntity = restTemplate.exchange(url, httpMethod, new HttpEntity<>(body, httpHeaders), String.class, uriParameters);
            logResponse(responseEntity);
            response = responseExtractor.extractResponse(responseEntity);
        } catch (HttpStatusCodeException ex) {
            logErrorResponse(ex);
            response = responseExtractor.extractErrorResponse(ex);
        }


        return response;
    }

    private HttpHeaders copyHeaders(HttpHeaders httpHeaders) {
        HttpHeaders headers = new HttpHeaders();

        for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
            headers.put(entry.getKey(), entry.getValue());
        }

        return headers;
    }


    /**
     * @param url               - the full service url and controller endpoint with url
     *                          parameters appended
     * @param headers           - additional headers
     * @param httpMethod        - the HTTP request method
     * @param requestBody       - the JSON request body
     * @param responseExtractor - the JSON response extractor for retrieving Java objects from
     *                          the response
     * @return object of the Type the responseExtractor retrieves
     */
    public <T> T executeRequest(String url, HttpMethod httpMethod, MultiValueMap<String, ?> headers, String requestBody,
                                RestResponseExtractor<T> responseExtractor) {

        return executeRequest(url, httpMethod, headers, requestBody, new HashMap<String, String>(), responseExtractor);
    }


    /**
     * @param url               - the full service url and controller endpoint with url
     *                          parameters appended
     * @param httpMethod        - the HTTP request method
     * @param headers           - the additional headers
     * @param uriParameters     - the URI parameters (e.g., path variables)
     * @param responseExtractor - the JSON response extractor for retrieving Java objects from
     *                          the response
     * @return object of the Type the responseExtractor retrieves
     */
    public <T> T executeRequest(String url, HttpMethod httpMethod, MultiValueMap<String, ?> headers, Map<String, ?> uriParameters,
                                RestResponseExtractor<T> responseExtractor) {

        return executeRequest(url, httpMethod, headers, null, uriParameters, responseExtractor);
    }

    /**
     * @param url               - the full service url and controller endpoint with url
     *                          parameters appended
     * @param httpMethod        - the HTTP request method
     * @param headers           - the additional headers
     * @param responseExtractor - the JSON response extractor for retrieving Java objects from
     *                          the response
     * @return object of the Type the responseExtractor retrieves
     */
    public <T> T executeRequest(String url, HttpMethod httpMethod, MultiValueMap<String, ?> headers,
                                RestResponseExtractor<T> responseExtractor) {

        return executeRequest(url, httpMethod, headers, null, new HashMap<String, String>(), responseExtractor);
    }

    /**
     * @param url               - the full service url and controller endpoint with url
     *                          parameters appended
     * @param httpMethod        - the HTTP request method
     * @param responseExtractor - the JSON response extractor for retrieving Java objects from
     *                          the response
     * @param uriParameters     - the uri Parameters
     * @return object of the Type the responseExtractor retrieves
     */
    public <T> T executeRequest(String url, HttpMethod httpMethod, Map<String, ?> uriParameters,
                                RestResponseExtractor<T> responseExtractor) {

        return executeRequest(url, httpMethod, new HttpHeaders(), null, uriParameters, responseExtractor);
    }

    /**
     * @param url               - the full service url and controller endpoint with url
     *                          parameters appended
     * @param httpMethod        - the HTTP request method
     * @param requestBody       - the JSON request body
     * @param responseExtractor - the JSON response extractor for retrieving Java objects from
     *                          the response
     * @return object of the Type the responseExtractor retrieves
     */
    public <T> T executeRequest(String url, HttpMethod httpMethod, String requestBody,
                                RestResponseExtractor<T> responseExtractor) {

        return executeRequest(url, httpMethod, new HttpHeaders(), requestBody, new HashMap<String, String>(), responseExtractor);
    }

    /**
     * @param url               - the full service url and controller endpoint with url
     *                          parameters appended
     * @param httpMethod        - the HTTP request method
     * @param responseExtractor - the JSON response extractor for retrieving Java objects from
     *                          the response
     * @return object of the Type the responseExtractor retrieves
     */
    public <T> T executeRequest(String url, HttpMethod httpMethod,
                                RestResponseExtractor<T> responseExtractor) {

        return executeRequest(url, httpMethod, new HttpHeaders(), null, new HashMap<String, String>(), responseExtractor);
    }

    /**
     * @param url           - the full service url and controller endpoint with url
     *                      parameters appended
     * @param httpMethod    - the HTTP request method
     * @param requestBody   - the JSON request body
     * @param uriParameters - the URI parameters (e.g., path variables)
     * @param responseType  - the response Java class type
     * @return object of the responseType class
     */
    public <T> T executeForParameterizedObject(String url, HttpMethod httpMethod, MultiValueMap<String, ?> headers,
                                               String requestBody, Map<String, ?> uriParameters, ParameterizedTypeReference<T> responseType) {
        logRequest(url, httpMethod, headers, requestBody, uriParameters);

        HttpEntity<String> httpEntity = null;
        if (authorization == null || authorization instanceof NoAuthorization) {
            httpEntity = NsfRestUtils.createHttpEntity(requestBody, headers != null ? headers : null);
        } else {
            httpEntity = NsfRestUtils.createHttpEntity(requestBody, headers != null ? headers : null, authorization);
        }
        ResponseEntity<T> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, responseType, uriParameters);
            logResponse(responseEntity);
        } catch (HttpStatusCodeException ex) {
            logErrorResponse(ex);
            throw ex;
        }

        return responseEntity.getBody();
    }


    /**
     * @param url          - the full service url and controller endpoint with url
     *                     parameters appended
     * @param headers      - additional headers
     * @param httpMethod   - the HTTP request method
     * @param requestBody  - the JSON request body
     * @param responseType - the response parameterized Java class type
     * @return object of the responseType class
     */
    public <T> T executeForParameterizedObject(String url, HttpMethod httpMethod, MultiValueMap<String, ?> headers,
                                               String requestBody, ParameterizedTypeReference<T> responseType) {

        return executeForParameterizedObject(url, httpMethod, headers, requestBody, new HashMap<String, String>(), responseType);
    }


    /**
     * @param url           - the full service url and controller endpoint with url
     *                      parameters appended
     * @param httpMethod    - the HTTP request method
     * @param headers       - the additional headers
     * @param uriParameters - the URI parameters (e.g., path variables)
     * @param responseType  - the response Java class type
     * @return object of the responseType class
     */
    public <T> T executeForParameterizedObject(String url, HttpMethod httpMethod, MultiValueMap<String, ?> headers, Map<String, ?> uriParameters,
                                               ParameterizedTypeReference<T> responseType) {

        return executeForParameterizedObject(url, httpMethod, headers, null, uriParameters, responseType);
    }

    /**
     * @param url          - the full service url and controller endpoint with url
     *                     parameters appended
     * @param httpMethod   - the HTTP request method
     * @param headers      - the additional headers
     * @param responseType - the response parameterized Java class type
     * @return object of the responseType class
     */
    public <T> T executeForParameterizedObject(String url, HttpMethod httpMethod, MultiValueMap<String, ?> headers,
                                               ParameterizedTypeReference<T> responseType) {

        return executeForParameterizedObject(url, httpMethod, headers, null, new HashMap<String, String>(), responseType);
    }

    /**
     * @param url           - the full service url and controller endpoint with url
     *                      parameters appended
     * @param httpMethod    - the HTTP request method
     * @param uriParameters - the uri Parameters
     * @param responseType  - the response Java class type
     * @return object of the responseType class
     */
    public <T> T executeForParameterizedObject(String url, HttpMethod httpMethod, Map<String, ?> uriParameters,
                                               ParameterizedTypeReference<T> responseType) {

        return executeForParameterizedObject(url, httpMethod, new HttpHeaders(), null, uriParameters, responseType);
    }

    /**
     * @param url          - the full service url and controller endpoint with url
     *                     parameters appended
     * @param httpMethod   - the HTTP request method
     * @param requestBody  - the JSON request body
     * @param responseType - the response parameterized Java class type
     * @return object of the responseType class
     */
    public <T> T executeForParameterizedObject(String url, HttpMethod httpMethod, String requestBody,
                                               ParameterizedTypeReference<T> responseType) {

        return executeForParameterizedObject(url, httpMethod, new HttpHeaders(), requestBody, new HashMap<String, String>(), responseType);
    }

    /**
     * @param url          - the full service url and controller endpoint with url
     *                     parameters appended
     * @param httpMethod   - the HTTP request method
     * @param responseType - the response parameterized Java class type
     * @return object of the responseType class
     */
    public <T> T executeForParameterizedObject(String url, HttpMethod httpMethod,
                                               ParameterizedTypeReference<T> responseType) {

        return executeForParameterizedObject(url, httpMethod, new HttpHeaders(), null, new HashMap<String, String>(), responseType);
    }

    /**
     * restTemplate getter
     *
     * @return RestTemplate
     */
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    /**
     * authorization getter
     *
     * @return HttpAuthorization
     */
    public HttpAuthorization getAuthorization() {
        return authorization;
    }

    /**
     * Logs outgoing request
     *
     * @param url
     * @param httpMethod
     * @param headers
     * @param requestBody
     * @param uriParameters
     */
    private void logRequest(String url, HttpMethod httpMethod, final MultiValueMap<String, ?> headers, String requestBody, final Map<String, ?> uriParameters) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n************************************************************************\n");
        sb.append("********************** NsfRestClient Sent Request **********************\n");
        sb.append("************************************************************************\n");
        sb.append("URL: " + formatURL(url, uriParameters));
        sb.append("\n");
        sb.append("HttpMethod: " + httpMethod);
        sb.append("\n");
        sb.append("Headers: " + headers.toSingleValueMap());
        if (LOGGER.isDebugEnabled()) {
            sb.append("\n");
            sb.append("RequestBody: " + (StringUtils.isNotBlank(requestBody) ? requestBody : "None"));
        }
        sb.append("\n");
        sb.append("**********************************End***********************************\n");
        sb.append("************************************************************************\n");

        LOGGER.info(sb.toString());
    }

    /**
     * Logs incoming response
     *
     * @param response
     */
    private void logResponse(ResponseEntity<?> response) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n************************************************************************\n");
        sb.append("********************** NsfRestClient Received Response *****************\n");
        sb.append("************************************************************************\n");
        sb.append("HttpStatusCode: " + response.getStatusCodeValue());
        sb.append("\n");
        sb.append("Headers: " + response.getHeaders());
        if (LOGGER.isDebugEnabled()) {
            sb.append("\n");
            sb.append("ResponseBody: \n" + (response.getBody() != null ? response.getBody() : "None"));
        }
        sb.append("\n");
        sb.append("**********************************End***********************************\n");
        sb.append("************************************************************************\n");

        LOGGER.info(sb.toString());
    }

    /**
     * Logs incoming error response
     *
     * @param ex
     */
    private void logErrorResponse(HttpStatusCodeException ex) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n************************************************************************\n");
        sb.append("********************** NsfRestClient Received Response *****************\n");
        sb.append("************************************************************************\n");
        sb.append("HttpStatusCode: " + ex.getStatusCode().value());
        sb.append("\n");
        sb.append("Headers: " + ex.getResponseHeaders());
        if (LOGGER.isDebugEnabled()) {
            sb.append("\n");
            sb.append("ResponseBody: \n" + (StringUtils.isNotBlank(ex.getResponseBodyAsString()) ? ex.getResponseBodyAsString() : "None"));
        }
        sb.append("\n");
        sb.append("**********************************End***********************************\n");
        sb.append("************************************************************************\n");

        LOGGER.info(sb.toString());
    }

    /**
     * Get log string for URL
     *
     * @param url
     * @param uriParameters
     * @return String
     */
    private String formatURL(String url, Map<String, ?> uriParameters) {
        String URL = url;
        for (Map.Entry<String, ?> entry : uriParameters.entrySet()) {
            URL = URL.replace("{" + entry.getKey() + "}", (String) entry.getValue());
        }
        try {
            return URLDecoder.decode(URL, "UTF-8");
        } catch (Exception e) {
            return URL;
        }
    }
}