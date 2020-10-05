package gov.nsf.components.rest.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nsf.components.rest.model.message.NsfResponseMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.CollectionUtils;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Generated("org.jsonschema2pojo")
public class BaseResponse {

    @ApiModelProperty(name = "errors", position = 0)
    private List<NsfResponseMessage> errors;

    @ApiModelProperty(name = "errors", position = 1)
    private List<NsfResponseMessage> warnings;

    @ApiModelProperty(name = "errors", position = 2)
    private List<NsfResponseMessage> informationals;

    public BaseResponse() {
        setErrors(new ArrayList<NsfResponseMessage>());
        setWarnings(new ArrayList<NsfResponseMessage>());
        setInformationals(new ArrayList<NsfResponseMessage>());
    }

    public BaseResponse(List<NsfResponseMessage> errors, List<NsfResponseMessage> warnings, List<NsfResponseMessage> informationals) {
        this.errors = errors;
        this.warnings = warnings;
        this.informationals = informationals;
    }

    public void addError(NsfResponseMessage error) {
        if (this.errors == null) {
            this.errors = new ArrayList<NsfResponseMessage>();
        }

        this.errors.add(error);
    }

    public void addWarning(NsfResponseMessage warning) {
        if (this.warnings == null) {
            this.warnings = new ArrayList<NsfResponseMessage>();
        }

        this.warnings.add(warning);
    }

    public void addInformational(NsfResponseMessage info) {
        if (this.informationals == null) {
            this.informationals = new ArrayList<NsfResponseMessage>();
        }

        this.informationals.add(info);
    }

    public boolean hasErrors() {
        return !CollectionUtils.isEmpty(getErrors());
    }

    public boolean hasWarnings() {
        return !CollectionUtils.isEmpty(getWarnings());
    }

    public boolean hasInformationals() {
        return CollectionUtils.isEmpty(getInformationals());
    }

    public List<NsfResponseMessage> getErrors() {
        return errors;
    }

    public void setErrors(List<NsfResponseMessage> errors) {
        this.errors = errors;
    }

    public List<NsfResponseMessage> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<NsfResponseMessage> warnings) {
        this.warnings = warnings;
    }

    public List<NsfResponseMessage> getInformationals() {
        return informationals;
    }

    public void setInformationals(List<NsfResponseMessage> informationals) {
        this.informationals = informationals;
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
        return CollectionUtils.isEmpty(errors) && CollectionUtils.isEmpty(warnings) && CollectionUtils.isEmpty(informationals);
    }
}
