package gov.nsf.components.rest.extractor;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JsonResponseExtractor
 */
public abstract class JsonResponseExtractor {

    private final ObjectMapper mapper = new ObjectMapper();
    private boolean sideLoaded;

    public JsonResponseExtractor(boolean sideLoaded) {
        this.sideLoaded = sideLoaded;
    }

    public ObjectMapper getMapper() {
        return this.mapper;
    }

    public boolean isSideLoaded() {
        return this.sideLoaded;
    }
}
