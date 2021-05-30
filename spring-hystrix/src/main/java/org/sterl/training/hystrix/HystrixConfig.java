package org.sterl.training.hystrix;

import java.util.Arrays;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

@Configuration
public class HystrixConfig {

    @Autowired
    private Environment env;

    @EventListener({
            ContextRefreshedEvent.class, // on spring start
            EnvironmentChangeEvent.class // on configuration changes, requires spring-cloud
        }
    )
    public void onRefresh(ApplicationContextEvent event) {
        final MutablePropertySources mps = ((AbstractEnvironment)this.env).getPropertySources();
        StreamSupport.stream(mps.spliterator(), false)
            .filter(ps -> ps instanceof EnumerablePropertySource)
            .map(ps -> ((EnumerablePropertySource<?>)ps).getPropertyNames())
            .flatMap(Arrays::<String>stream)
            .filter(ps -> ps.startsWith("hystrix."))
            .forEach(propName -> {
                ConfigurationManager.getConfigInstance().setProperty(propName, this.env.getProperty(propName));
                System.err.println(propName + "\t= " + this.env.getProperty(propName));
            });
        
    }
    
    @Bean
    public ServletRegistrationBean<HystrixMetricsStreamServlet> hystrixStreamServlet(){
        return new ServletRegistrationBean<>(new HystrixMetricsStreamServlet(), "/hystrix.stream");
    }
}
