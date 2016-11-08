package org.sterl.training.vaadin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SpringVaadinDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringVaadinDashboardApplication.class, args);
    }
    
    /*
    @VaadinServletConfiguration( widgetset = "org.sterl.training.vaadin.DashboardWidgetSet", productionMode = false, ui = DashboardUI.class, closeIdleSessions = true)
    public static class AppServlet extends VaadinServlet {
    }
    
    @Bean
    public ServletRegistrationBean vaadin() {
      return new ServletRegistrationBean(new AppServlet(), "/app/*", "/VAADIN/*");
    }
    */
}
