package org.sterl.training.vaadin;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gargoylesoftware.htmlunit.WebClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringVaadinDashboardApplicationTests {
    private static final String MANDATORY_BOOTSTRAP_PART = "log('Vaadin bootstrap loaded');";

    private final WebClient webClient = new WebClient();

    @LocalServerPort
    private int port;

    @Test
    public void contextLoads() throws Exception {
        String javaScriptUrl = "http://localhost:" + port + "/VAADIN/vaadinBootstrap.js?v=8.0-SNAPSHOT";
        String content = this.webClient.getPage(javaScriptUrl).getWebResponse().getContentAsString();
        Assert.assertTrue("Mandatory part of bootstrap is not found", content.contains(MANDATORY_BOOTSTRAP_PART));
    }

}
