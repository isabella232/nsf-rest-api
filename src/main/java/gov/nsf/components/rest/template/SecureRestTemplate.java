package gov.nsf.components.rest.template;

import gov.nsf.components.rest.exception.HttpClientInitializationException;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * SecureRestTemplate class for RestTemplate w/ SSL
 */
@Deprecated
public class SecureRestTemplate extends PoolingRestTemplate {

    public SecureRestTemplate(){
        super();
    }

    public SecureRestTemplate(int requestTimeout) {
        super(requestTimeout);
    }

    public SecureRestTemplate(int requestTimeout, int maxConnectionsPerRoute, int maxTotalConnections) {
        super(requestTimeout, maxConnectionsPerRoute, maxTotalConnections);
    }

    @Override
    public HttpClientBuilder buildHttpClient() throws HttpClientInitializationException {

        SSLContext sslContext = null;
        try {
            sslContext = getSSLContext();
        } catch (KeyManagementException ex) {
            throw new HttpClientInitializationException("Could not create SSL Socket Factory", ex);
        } catch (NoSuchAlgorithmException nsae) {
            throw new HttpClientInitializationException("Could not create SSL Socket Factory", nsae);
        }

        return super.buildHttpClient()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext))
                .setSSLHostnameVerifier(new DefaultHostnameVerifier());
    }

    private SSLContext getSSLContext() throws KeyManagementException, NoSuchAlgorithmException {

        return SSLContexts.custom()
                .useProtocol("TLSv1")
                .build();
    }
}
