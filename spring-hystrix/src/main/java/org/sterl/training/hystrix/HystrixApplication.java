package org.sterl.training.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

@SpringBootApplication
public class HystrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixApplication.class, args);
    }
    
    @Bean
    public ServletRegistrationBean<HystrixMetricsStreamServlet> hystrixStreamServlet(){
        return new ServletRegistrationBean<>(new HystrixMetricsStreamServlet(), "/hystrix.stream");
    }
}
