package gov.nsf.components.rest.example.service;

import gov.nsf.components.rest.example.model.Award;

import java.util.Collection;
import java.util.Map;

/**
 * AwardService
 */
public interface AwardService {

    Award getAwardById(String id);

    Collection<Award> searchAwards(Map<String, Object> searchCriteria);

    Award createAward(Award award);


}
