package org.sterl.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.sterl.jpa.custom_type.model.Zip;

@SpringBootApplication
public class JpaHibernateApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(JpaHibernateApplication.class, args);
    }
    /*
    @Bean
    Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL)
          .serializers(LOCAL_DATETIME_SERIALIZER);
    }
    */
    
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(Zip.class, String.class, s -> s == null ? null : s.value());
    }
}

