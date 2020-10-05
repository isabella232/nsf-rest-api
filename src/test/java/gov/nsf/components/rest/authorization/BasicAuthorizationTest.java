package gov.nsf.components.rest.authorization;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * JUnit tests for BasicAuthorization
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class BasicAuthorizationTest {

    private static final String USERNAME = "TestSvc";
    private static final String PASSWORD = "TestSvc123";
    private static final String EXPECTED_HEADER = "Authorization";
    private static final String EXPECTED_VALUE = "Basic VGVzdFN2YzpUZXN0U3ZjMTIz";

    private BasicAuthentication authentication;

    @Before
    public void setup() {
        authentication = new BasicAuthentication(USERNAME, PASSWORD);
    }

    @Test
    public void testGenerateHeaders() {
        MultiValueMap<String, ?> result = authentication.generateAuthorizationHeaders();
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        Assert.assertTrue(result.containsKey(EXPECTED_HEADER));
        Assert.assertEquals(EXPECTED_VALUE, result.get(EXPECTED_HEADER).get(0));
    }
}
