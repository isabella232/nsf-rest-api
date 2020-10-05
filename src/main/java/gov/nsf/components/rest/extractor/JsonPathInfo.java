package gov.nsf.components.rest.extractor;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * JsonPathInfo
 */
public class JsonPathInfo {

    private String rootName;
    private String dataName;

    public JsonPathInfo(String rootName, String dataName){
        this.rootName = rootName;
        this.dataName = dataName;
    }

    public JsonPathInfo(){
        this.rootName = null;
        this.dataName = null;
    }

    public String getRootName() {
        return rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
}
