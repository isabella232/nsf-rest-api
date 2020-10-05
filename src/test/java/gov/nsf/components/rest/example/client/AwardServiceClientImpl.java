package gov.nsf.components.rest.example.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nsf.components.rest.client.NsfRestClient;
import gov.nsf.components.rest.example.model.Award;
import gov.nsf.components.rest.example.service.AwardService;
import gov.nsf.components.rest.extractor.ListExtractor;
import gov.nsf.components.rest.extractor.ModelExtractor;
import gov.nsf.components.rest.util.NsfRestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Award Service REST Client
 */
public class AwardServiceClientImpl extends NsfRestClient implements AwardService {

    private String awardServiceURL;

    @Override
    public Award getAwardById(String id) {
        String endpoint = awardServiceURL + "/awards/{id}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_PDF));
        return executeRequest(endpoint,
                HttpMethod.GET,
                headers,
                Collections.singletonMap("id", id),
                new ModelExtractor<Award>(Award.class, "award"));
    }

    @Override
    public Collection<Award> searchAwards(Map<String, Object> searchCriteria) {
        String endpoint = awardServiceURL + "/awards";
        String fullURI = NsfRestUtils.buildFullURI(endpoint, searchCriteria);

        return executeRequest(fullURI,
                HttpMethod.GET,
                new ListExtractor<Award>(Award.class, "awards"));
    }

    @Override
    public Award createAward(Award award) {
        String endpoint = awardServiceURL + "/awards";
        String jsonBody = convertToJson(award);

        return executeRequest(endpoint,
                HttpMethod.POST,
                jsonBody,
                new ModelExtractor<Award>(Award.class, "award"));
    }

    /**
     * Helper method for converting Java object to JSON
     *
     * @param obj
     * @return String json
     */
    private static String convertToJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    public String getAwardServiceURL() {
        return awardServiceURL;
    }

    public void setAwardServiceURL(String awardServiceURL) {
        this.awardServiceURL = awardServiceURL;
    }
}
