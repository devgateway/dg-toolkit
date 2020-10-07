/**
 *
 */
package org.devgateway.toolkit.web.fm;

import org.devgateway.toolkit.web.AbstractWebTest;
import org.devgateway.toolkit.web.fm.service.DgFmService;
import org.devgateway.toolkit.web.rest.controller.DummyController;
import org.devgateway.toolkit.web.rest.entity.Dummy;
import org.devgateway.toolkit.web.spring.AsyncControllerLookupService;
import org.devgateway.toolkit.web.spring.util.AsyncBeanParamControllerMethodCallable;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author mihai
 *
 */
@TestPropertySource("classpath:testFm.properties")
public class DgFmTest extends AbstractWebTest {

    @Autowired
    private DgFmService fmService;

    @Test
    public void testFmInit() {
        Assert.assertTrue("Must have at least one feature", fmService.featuresCount() > 0);
        Assert.assertNotNull(fmService.getFeature("testField"));
        Assert.assertTrue(fmService.getFeature("testField").getEnabled());
        Assert.assertFalse(fmService.getFeature("testField").getVisible());
    }

    @Test
    public void testMandatory() {
        Assert.assertFalse(fmService.getFeature("field").getMandatory());
        Assert.assertTrue(fmService.getFeature("testField").getMandatory());
        Assert.assertEquals(FmConstants.DEFAULT_MANDATORY,
                fmService.getFeature("tenderField").getMandatory());
    }

    @Test
    public void testEnabled() {
        Assert.assertFalse(fmService.getFeature("field").getEnabled());
        Assert.assertFalse(fmService.getFeature("testField").getEnabled());
        Assert.assertEquals(FmConstants.DEFAULT_ENABLED, fmService.getFeature("tenderChart").getEnabled());
    }

    @Test
    public void testVisible() {
        Assert.assertFalse(fmService.getFeature("field").getEnabled());
        Assert.assertFalse(fmService.getFeature("testField").getEnabled());
        Assert.assertEquals(FmConstants.DEFAULT_VISIBLE, fmService.getFeature("tenderChart").getEnabled());
    }
}
