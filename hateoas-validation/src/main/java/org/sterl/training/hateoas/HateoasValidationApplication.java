package org.sterl.training.hateoas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@SpringBootApplication
public class HateoasValidationApplication implements RepositoryRestConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(HateoasValidationApplication.class, args);
	}

	@Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
	    config.exposeIdsFor(ValidatedBean.class);
	    config.setBasePath("/api");
    }
}
