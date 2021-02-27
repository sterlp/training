package org.sterl.training.redis.template.api;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Configuration
public class HelloRedisFunctional {

    @Service
    public static class RedisHandler {
        @Autowired ReactiveStringRedisTemplate reactiveRedis;

        public Mono<ServerResponse> saveAndGet(ServerRequest request) {
            final Mono<String> body = request.bodyToMono(String.class);
            
            final Mono<Boolean> saved = body.
                    flatMap(msg -> reactiveRedis.opsForValue().set("message", msg + " " + Instant.now()));

            final Mono<String> loaded = saved.then(reactiveRedis.opsForValue().get("message"));

            return ServerResponse.ok().body(loaded, String.class);
        }
    }


    @Bean
    public RouterFunction<ServerResponse> route(RedisHandler redisHandler) {

      return RouterFunctions
        .route(RequestPredicates.POST("/redis/functional").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), 
                redisHandler::saveAndGet);
    }
}
