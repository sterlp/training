package org.sterl.gcm.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.sterl.gcm._example.server.GcmServerApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GcmServerApplication.class)
@WebAppConfiguration
public class GcmServerApplicationTests {

	@Test
	public void contextLoads() {
	}

}
