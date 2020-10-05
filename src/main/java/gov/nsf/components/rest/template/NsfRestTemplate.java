package gov.nsf.components.rest.template;

import gov.nsf.components.rest.exception.HttpClientInitializationException;
import gov.nsf.components.rest.exception.RestTemplateInitializationException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * NsfRestTemplate class
 * <p>
 * Sub-class templates must use the
 */
@Deprecated
public abstract class NsfRestTemplate extends RestTemplate {

    private int timeout;

    /**
     * initializeTemplate post-construct method sets the request factory that was initialized from the
     * subclasses implementation of the buildHttpClient() method
     *
     * @throws RestTemplateInitializationException
     */
    @PostConstruct
    public void initializeTemplate() throws RestTemplateInitializationException {
        setRequestFactory(new HttpComponentsClientHttpRequestFactory(buildHttpClient().build()));
    }

    /**
     * Subclasses must build the HttpClient to their desired extend
     *
     * @return HttpClientBuilder
     * @throws HttpClientInitializationException
     */
    public abstract HttpClientBuilder buildHttpClient() throws HttpClientInitializationException;

    /**
     * Default requestConfig helper method
     *
     * @param requestTimeout
     * @return RequestConfig
     */
    public RequestConfig getRequestConfig(int requestTimeout) {

        return RequestConfig.custom()
                .setConnectionRequestTimeout(requestTimeout)
                .setConnectTimeout(requestTimeout)
                .setSocketTimeout(requestTimeout)
                .setRedirectsEnabled(true)
                .setRelativeRedirectsAllowed(true)
                .build();
    }

    /**
     * Timeout getter
     *
     * @return int timeout in MS
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Timeout setter
     *
     * @param timeout in MS
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
