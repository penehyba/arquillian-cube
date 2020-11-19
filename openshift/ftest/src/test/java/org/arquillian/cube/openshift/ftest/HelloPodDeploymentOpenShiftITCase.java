package org.arquillian.cube.openshift.ftest;

import java.net.URL;
import org.arquillian.cube.openshift.impl.requirement.RequiresOpenshift;
import org.arquillian.cube.remote.requirement.RequiresRemoteResource;
import org.arquillian.cube.requirement.ArquillianConditionalRunner;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@Category({RequiresOpenshift.class, RequiresRemoteResource.class})
@RequiresOpenshift
@Ignore
@RunWith(ArquillianConditionalRunner.class)
public class HelloPodDeploymentOpenShiftITCase {

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive deploy() {
        System.out.println("HelloPodDeploymentOpenShiftITCase deploying...");
        return ShrinkWrap.create(WebArchive.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void shouldBeAbleToInjectURL() throws Exception {
        System.out.println(this.getClass() + "Base URL: " + base);
        Assert.assertNotNull(base);
    }
}
