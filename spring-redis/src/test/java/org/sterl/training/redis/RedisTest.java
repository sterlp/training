package org.sterl.training.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.sterl.training.redis.model.CachedEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RedisTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    private String baseUrl;

    @BeforeEach
    void before() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void test() {
        restTemplate.delete(baseUrl + "/redis");
        
        ResponseEntity<CachedEntity> e = restTemplate.getForEntity(baseUrl + "/api/person/1", CachedEntity.class);
        assertThat(e).isNotNull();
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(e.getBody().getId()).isEqualTo("1");
        
        e = restTemplate.getForEntity(baseUrl + "/api/person/2", CachedEntity.class);
        assertThat(e).isNotNull();
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(e.getBody().getId()).isEqualTo("2");
        
        final ResponseEntity<List<CachedEntity>> elements = restTemplate.exchange(baseUrl + "/api/data", 
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CachedEntity>>() {});
        
        assertThat(elements.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(elements.getBody().size()).isEqualTo(2);
    }

}
