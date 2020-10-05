package gov.nsf.components.rest.model.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "NsfResponseMessage", description = "Standard NSF Error/Warning/Informational object")
public class NsfResponseMessage {

    @ApiModelProperty(name = "id", value = "the unique identifier", position = 0)
    private String id;

    @ApiModelProperty(name = "title", value = "the short title", position = 1)
    private String title;

    @ApiModelProperty(name = "description", value = "the description of the message", position = 2)
    private String description;

    @ApiModelProperty(name = "httpStatus", value = "the http status code", position = 3)
    private Integer httpStatus;

    @ApiModelProperty(name = "source", value = "the source of the message", position = 4)
    private Source source;

    @ApiModelProperty(name = "meta", value = "a meta object", position = 5)
    private Object meta;

    public NsfResponseMessage() {

    }

    public NsfResponseMessage(String title, String description, Integer httpStatus) {
        super();
        this.title = title;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    public NsfResponseMessage(String title, String description, Integer httpStatus,
                              Source source, Object meta) {
        super();
        this.title = title;
        this.description = description;
        this.httpStatus = httpStatus;
        this.source = source;
        this.meta = meta;
    }

    public NsfResponseMessage(String id, String title, String description, Integer httpStatus,
                              Source source, Object meta) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.httpStatus = httpStatus;
        this.source = source;
        this.meta = meta;
    }

    @Deprecated
    public NsfResponseMessage(String id, String title, String description, Integer httpStatus, NsfMessageCode code,
                              Source source, Object meta) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.httpStatus = httpStatus;
        this.source = source;
        this.meta = meta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Object getMeta() {
        return meta;
    }

    public void setMeta(Object meta) {
        this.meta = meta;
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
