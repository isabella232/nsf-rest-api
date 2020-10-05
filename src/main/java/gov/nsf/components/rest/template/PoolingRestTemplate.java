package gov.nsf.components.rest.template;

import gov.nsf.components.rest.exception.HttpClientInitializationException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * PoolingRestTemplate
 */
@Deprecated
public class PoolingRestTemplate extends NsfRestTemplate {

    private static final int DEFAULT_MAX_TOTAL_CONN = 20;
    private static final int DEFAULT_MAX_CONN_PER_ROUTE = 4;
    private static final int DEFAULT_TIMEOUT = 60000; //60 seconds

    private int maxTotalConnections;
    private int maxConnectionsPerRoute;

    public PoolingRestTemplate(){
        setTimeout(DEFAULT_TIMEOUT);
        setMaxConnectionsPerRoute(DEFAULT_MAX_CONN_PER_ROUTE);
        setMaxTotalConnections(DEFAULT_MAX_TOTAL_CONN);
    }

    public PoolingRestTemplate(int requestTimeout) {
        setTimeout(requestTimeout);
        setMaxConnectionsPerRoute(DEFAULT_MAX_CONN_PER_ROUTE);
        setMaxTotalConnections(DEFAULT_MAX_TOTAL_CONN);
    }

    public PoolingRestTemplate(int requestTimeout, int maxConnectionsPerRoute, int maxTotalConnections) {
        setTimeout(requestTimeout);
        setMaxConnectionsPerRoute(maxConnectionsPerRoute);
        setMaxTotalConnections(maxTotalConnections);
    }

    @Override
    public HttpClientBuilder buildHttpClient() throws HttpClientInitializationException {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

        connectionManager.setDefaultMaxPerRoute(getMaxConnectionsPerRoute());
        connectionManager.setMaxTotal(getMaxTotalConnections());

        return HttpClients.custom()
                .setDefaultRequestConfig(getRequestConfig(getTimeout()))
                .setConnectionManager(connectionManager);
    }

    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public int getMaxConnectionsPerRoute() {
        return maxConnectionsPerRoute;
    }

    public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
    }
}
