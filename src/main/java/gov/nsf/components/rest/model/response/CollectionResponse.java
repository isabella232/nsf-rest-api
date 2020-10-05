package gov.nsf.components.rest.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * Collection Wrapper class of type <T>
 *
 * @param <T> data type
 */
public class CollectionResponse<T> extends BaseResponse {

    private Collection<T> data;

    public CollectionResponse() {
        super();
    }

    @JsonCreator
    public CollectionResponse(@JsonProperty("data") Collection<T> data) {
        super();
        this.data = data;
    }

    public boolean hasData( ){
        return !CollectionUtils.isEmpty(getData());
    }

    public Collection<T> getData() {
        return data;
    }

    public void setData(Collection<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "ERROR DURING TO_STRING";
        }
    }

    @JsonIgnore
    public boolean isEmpty(){
        return super.isEmpty() && CollectionUtils.isEmpty(data);
    }
}
