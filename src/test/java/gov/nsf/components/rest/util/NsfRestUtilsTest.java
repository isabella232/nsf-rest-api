package gov.nsf.components.rest.util;

import gov.nsf.components.rest.authorization.BasicAuthentication;
import gov.nsf.components.rest.authorization.HttpAuthorization;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.*;

/**
 * JUnit tests for NsfRestUtils
 */
public class NsfRestUtilsTest {

    @Test
    public void testBuildFullURI_happyPath() {
        String url = "http://testURL.com/service/api/users";

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("firstName", "Bob");
        parameters.put("lastName", "Smith");

        final String expectedResult_A = url + "?" + "firstName=Bob&lastName=Smith";
        final String expectedResult_B = url + "?" + "lastName=Smith&firstName=Bob";
        List<String> expected = new ArrayList<String>() {{
            add(expectedResult_A);
            add(expectedResult_B);
        }};
        String observedResult = NsfRestUtils.buildFullURI(url, parameters);

        Assert.assertNotNull(observedResult);
        Assert.assertTrue(expected.contains(observedResult));
    }

    @Test
    public void testBuildFullURI_listArgument() {
        String url = "http://testURL.com/service/api/users";
        List<String> listParameter = new ArrayList<String>() {{
            add("-param1");
            add("param2");
        }};

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("sort", listParameter);

        String expectedResult = url + "?" + "sort=-param1,param2";
        String observedResult = NsfRestUtils.buildFullURI(url, parameters);

        Assert.assertNotNull(observedResult);
        Assert.assertEquals(expectedResult, observedResult);
    }

    @Test
    public void testBuildFullURI_CollectionArgument() {
        String url = "http://testURL.com/service/api/users";
        Collection<String> listParameter = new ArrayList<String>() {{
            add("-param1");
            add("param2");
        }};

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("sort", listParameter);

        String expectedResult = url + "?" + "sort=-param1,param2";
        String observedResult = NsfRestUtils.buildFullURI(url, parameters);

        Assert.assertNotNull(observedResult);
        Assert.assertEquals(expectedResult, observedResult);
    }

    @Test
    public void testBuildFullURI_ArrayArgument() {
        String url = "http://testURL.com/service/api/users";
        String[] arrayParameter = new String[]{"-param1", "param2"};

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("sort", arrayParameter);

        String expectedResult = url + "?" + "sort=-param1,param2";
        String observedResult = NsfRestUtils.buildFullURI(url, parameters);

        Assert.assertNotNull(observedResult);
        Assert.assertEquals(expectedResult, observedResult);
    }

    @Test
    public void testBuildFullURI_noParameters() {
        String url = "http://testURL.com/service/api/users";

        String expectedResult = url;
        String observedResult = NsfRestUtils.buildFullURI(url, new HashMap<String, String>());

        Assert.assertNotNull(observedResult);
        Assert.assertEquals(expectedResult, observedResult);
    }

    @Test
    public void testBuildFullURI_filterNullParameters() {
        String url = "http://testURL.com/service/api/users";

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(null, "Bob");
        parameters.put("lastName", "Smith");

        String expectedResult = url + "?" + "lastName=Smith";
        String observedResult = NsfRestUtils.buildFullURI(url, parameters);

        Assert.assertNotNull(observedResult);
        Assert.assertEquals(expectedResult, observedResult);
    }

    @Test
    public void testBuildFullURI_filterNullParameterValue() {
        String url = "http://testURL.com/service/api/users";

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("firstName", null);
        parameters.put("lastName", "Smith");

        String expectedResult = url + "?" + "lastName=Smith";
        String observedResult = NsfRestUtils.buildFullURI(url, parameters);

        Assert.assertNotNull(observedResult);
        Assert.assertEquals(expectedResult, observedResult);
    }

    @Test
    public void testBuildFullURI_filterEmptyParameters() {
        String url = "http://testURL.com/service/api/users";

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("", "Bob");
        parameters.put("lastName", "Smith");

        String expectedResult = url + "?" + "lastName=Smith";
        String observedResult = NsfRestUtils.buildFullURI(url, parameters);

        Assert.assertNotNull(observedResult);
        Assert.assertEquals(expectedResult, observedResult);
    }

    @Test
    public void testBuildFullURI_filterEmptyParameterValue() {
        String url = "http://testURL.com/service/api/users";

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("firstName", "");
        parameters.put("lastName", "Smith");

        String expectedResult = url + "?" + "lastName=Smith";
        String observedResult = NsfRestUtils.buildFullURI(url, parameters);

        Assert.assertNotNull(observedResult);
        Assert.assertEquals(expectedResult, observedResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildFullURI_nullURL() {
        NsfRestUtils.buildFullURI(null, new HashMap<String, String>());
    }

    @Test
    public void testBuildFullURI_nullParameters() {
        String url = NsfRestUtils.buildFullURI("http://testURL.com/service/api/users", null);
        Assert.assertEquals("http://testURL.com/service/api/users", url);
    }

    @Test
    public void testCreateHttpEntity_JustBody() {
        String testBody = "{ data: \"DATA\"}";
        HttpEntity<String> entity = NsfRestUtils.createHttpEntity(testBody);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getBody());
        Assert.assertNotNull(entity.getHeaders());
        Assert.assertEquals(testBody, entity.getBody());
        assertBaseHeaders(entity);
    }

    @Test
    public void testCreateHttpEntity_WithAuth() {
        String testBody = "{ data: \"DATA\"}";
        HttpAuthorization authorization = new BasicAuthentication("USERNAME", "PASSWORD");
        HttpEntity<String> entity = NsfRestUtils.createHttpEntity(testBody, authorization);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getBody());
        Assert.assertNotNull(entity.getHeaders());
        Assert.assertEquals(testBody, entity.getBody());
        assertBaseHeaders(entity);
        Assert.assertTrue(entity.getHeaders().containsKey("Authorization"));
        Assert.assertEquals(entity.getHeaders().get("Authorization").get(0), "Basic VVNFUk5BTUU6UEFTU1dPUkQ=");
    }

    @Test
    public void testCreateHttpEntity_WithAdditionalHeader() {
        String testBody = "{ data: \"DATA\"}";
        HttpHeaders headers = new HttpHeaders() {{
            add("proxy-remote-user", "bsmith");
        }};
        HttpEntity<String> entity = NsfRestUtils.createHttpEntity(testBody, headers);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getBody());
        Assert.assertNotNull(entity.getHeaders());
        Assert.assertEquals(testBody, entity.getBody());
        assertBaseHeaders(entity);
        Assert.assertTrue(entity.getHeaders().containsKey("proxy-remote-user"));
        Assert.assertTrue(entity.getHeaders().get("proxy-remote-user").contains("bsmith"));

    }

    @Test
    public void testCreateHttpEntity_WithAdditionalHeaderAndAuth() {
        String testBody = "{ data: \"DATA\"}";
        HttpAuthorization authorization = new BasicAuthentication("USERNAME", "PASSWORD");
        HttpHeaders headers = new HttpHeaders() {{
            add("proxy-remote-user", "bsmith");
        }};
        HttpEntity<String> entity = NsfRestUtils.createHttpEntity(testBody, headers, authorization);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getBody());
        Assert.assertNotNull(entity.getHeaders());
        Assert.assertEquals(testBody, entity.getBody());
        assertBaseHeaders(entity);
        System.out.println(entity.getHeaders());
        Assert.assertTrue(entity.getHeaders().containsKey("Authorization"));
        Assert.assertEquals(entity.getHeaders().get("Authorization").get(0), "Basic VVNFUk5BTUU6UEFTU1dPUkQ=");
        Assert.assertTrue(entity.getHeaders().containsKey("proxy-remote-user"));
        Assert.assertTrue(entity.getHeaders().get("proxy-remote-user").contains("bsmith"));

    }


    private void assertBaseHeaders(HttpEntity<String> entity) {
        Assert.assertTrue(entity.getHeaders().containsKey("Accept"));
        Assert.assertTrue(entity.getHeaders().get("Accept").contains(MediaType.APPLICATION_JSON_VALUE));
        Assert.assertTrue(entity.getHeaders().containsKey("Content-Type"));
        Assert.assertTrue(entity.getHeaders().get("Content-Type").contains(MediaType.APPLICATION_JSON_VALUE));
    }
}
