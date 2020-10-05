package gov.nsf.components.rest.util;


import gov.nsf.components.rest.authorization.HttpAuthorization;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Common REST Utility methods
 */
public class NsfRestUtils {

    //Sonar compliance
    private NsfRestUtils() {
        super();
    }

    public static String buildFullURI(String httpURL, Map<String, ?> queryParameters) {
        if (httpURL == null) {
            throw new IllegalArgumentException("URL must not be null");
        }

        if (MapUtils.isEmpty(queryParameters)) {
            return httpURL;
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpURL);
        for (Map.Entry<String, ?> entry : queryParameters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (StringUtils.isEmpty(key)
                    || value == null
                    || (value instanceof String && StringUtils.isEmpty((String) value))) {
                continue;
            }

            if (value instanceof Collection<?>) {
                value = StringUtils.join((Collection) value, ",");
            } else if (value instanceof Object[]) {
                value = StringUtils.join((Object[]) value, ",");
            } else if (value instanceof Iterator<?>) {
                value = StringUtils.join((Iterator<?>) value, ",");
            }

            builder.queryParam(key, value);
        }

        return builder.build().toUriString();

    }

    /**
     * Returns an HttpEntity object constructed with the passed JSON requestBody
     *
     * @param requestBody - JSON request body
     * @return HttpEntity
     */
    public static <T> HttpEntity<T> createHttpEntity(T requestBody) {
        return new HttpEntityBuilder<T>().
                withBody(requestBody).
                withHeaders(getBaseHeaders()).
                build();
    }

    /**
     * Returns an HttpEntity object constructed with the passed
     * HttpAuthorization and JSON body
     *
     * @param requestBody   - JSON request body
     * @param authorization - HttpAuthorization credentials
     * @return HttpEntity
     */
    public static <T> HttpEntity<T> createHttpEntity(T requestBody, HttpAuthorization authorization) {
        return new HttpEntityBuilder<T>()
                .withBody(requestBody)
                .withHeaders(getBaseHeaders())
                .withHeaders(authorization.generateAuthorizationHeaders())
                .build();
    }

    public static <T> HttpEntity<T> createHttpEntity(T requestBody, MultiValueMap<String, ?> headers) {
        return new HttpEntityBuilder<T>()
                .withBody(requestBody)
                .withHeaders(getBaseHeaders())
                .withHeaders(headers)
                .build();
    }

    public static <T> HttpEntity<T> createHttpEntity(T requestBody, MultiValueMap<String, ?> headers, HttpAuthorization authorization) {
        return new HttpEntityBuilder<T>()
                .withBody(requestBody)
                .withHeaders(getBaseHeaders())
                .withHeaders(headers)
                .withHeaders(authorization.generateAuthorizationHeaders())
                .build();
    }

    public static MultiValueMap<String, ?> getBaseHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }


    public static class HttpEntityBuilder<T> {

        private T body;
        private HttpHeaders headers;

        public HttpEntityBuilder() {
            this.headers = new HttpHeaders();
        }

        public HttpEntity<T> build() {
            return new HttpEntity<T>(body, headers);
        }

        public HttpEntityBuilder<T> withBody(T body) {
            this.body = body;
            return this;
        }

        public HttpEntityBuilder<T> withHeader(String header, String value) {
            this.headers.set(header, value);
            return this;
        }

        public HttpEntityBuilder<T> withHeader(String header, List<String> value) {
            this.headers.put(header, value);
            return this;
        }

        public HttpEntityBuilder<T> withHeaders(MultiValueMap<String, ?> headers) {
            if (headers != null) {
                for (Map.Entry<String, ?> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof List) {
                        this.headers.put(key, (List) value);
                    } else if (value instanceof String) {
                        this.headers.add(key, (String) value);
                    }
                }
            }
            return this;
        }
    }


}