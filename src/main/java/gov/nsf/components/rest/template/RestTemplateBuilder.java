package gov.nsf.components.rest.template;

import gov.nsf.components.rest.exception.HttpClientInitializationException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * RestTemplateBuilder
 */
public class RestTemplateBuilder {

    private int maxTotalConnections = 20;
    private int maxConnectionsPerRoute = 4;
    private int timeout = 60000;
    private String protocol = "TLSv1";
    private boolean enableSSL = true;

    public static RestTemplateBuilder custom() {
        return new RestTemplateBuilder();
    }

    public static RestTemplate defaultRestTemplate() {
        return new RestTemplateBuilder().build();
    }

    private RestTemplateBuilder() {

    }

    public RestTemplateBuilder withMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
        return this;
    }

    public RestTemplateBuilder withMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
        return this;
    }

    public RestTemplateBuilder withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public RestTemplateBuilder withProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public RestTemplate build() {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient()));
    }

    private CloseableHttpClient httpClient() {
        SSLContext sslContext = null;
        try {
            sslContext = getSSLContext();
        } catch (KeyManagementException ex) {
            throw new HttpClientInitializationException("Could not create SSL Socket Factory", ex);
        } catch (NoSuchAlgorithmException nsae) {
            throw new HttpClientInitializationException("Could not create SSL Socket Factory", nsae);
        }

        final HttpClientBuilder builder = HttpClients.custom()
                .setDefaultRequestConfig(getRequestConfig(this.timeout))
                .setConnectionManager(connectionManager());

        if (enableSSL) {
            builder.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext))
                    .setSSLHostnameVerifier(new DefaultHostnameVerifier());
        }

        return builder.build();
    }

    private PoolingHttpClientConnectionManager connectionManager() {
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(this.maxConnectionsPerRoute);
        connectionManager.setMaxTotal(this.maxTotalConnections);

        return connectionManager;
    }

    /**
     * Default requestConfig helper method
     *
     * @param requestTimeout
     * @return RequestConfig
     */
    private RequestConfig getRequestConfig(int requestTimeout) {

        return RequestConfig.custom()
                .setConnectionRequestTimeout(requestTimeout)
                .setConnectTimeout(requestTimeout)
                .setSocketTimeout(requestTimeout)
                .setRedirectsEnabled(true)
                .setRelativeRedirectsAllowed(true)
                .build();
    }


    private SSLContext getSSLContext() throws KeyManagementException, NoSuchAlgorithmException {

        return SSLContexts.custom()
                .setProtocol(this.protocol)
                .build();
    }

}
