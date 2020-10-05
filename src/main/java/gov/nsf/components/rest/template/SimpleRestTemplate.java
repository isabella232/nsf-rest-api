package gov.nsf.components.rest.template;

import gov.nsf.components.rest.exception.HttpClientInitializationException;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * SimpleRestTemplate class
 */
@Deprecated
public class SimpleRestTemplate extends PoolingRestTemplate {

    public SimpleRestTemplate(){
        super();
    }

    public SimpleRestTemplate(int requestTimeout) {
        super(requestTimeout);
    }

    @Override
    public HttpClientBuilder buildHttpClient() throws HttpClientInitializationException {
        return super.buildHttpClient();
    }
}
