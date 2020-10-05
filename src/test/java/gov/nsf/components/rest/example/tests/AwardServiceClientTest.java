package gov.nsf.components.rest.example.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.nsf.components.rest.example.client.AwardServiceClientImpl;
import gov.nsf.components.rest.template.SecureRestTemplate;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * AwardServiceClientTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/awardservice-client-config.xml"})
public class AwardServiceClientTest {

    @Autowired
    private AwardServiceClientImpl awardServiceClient;

    @Test
    @Ignore
    public void test() throws JsonProcessingException {
        awardServiceClient.getAwardById("1");
    }


}
