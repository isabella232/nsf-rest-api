package gov.nsf.components.rest.model.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Generated;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Source {

    private String pointer;
    private Object rejectedValue;

    public Source() {

    }

    public Source(String pointer) {
        this.pointer = pointer;
    }

    public Source(String pointer, String rejectedValue) {
        this.pointer = pointer;
        this.rejectedValue = rejectedValue;
    }

    public String getPointer() {
        return pointer;
    }

    public void setPointer(String pointer) {
        this.pointer = pointer;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(String rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "ERROR DURING TO_STRING";
        }
    }
}
