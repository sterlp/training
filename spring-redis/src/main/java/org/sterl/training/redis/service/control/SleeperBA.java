package org.sterl.training.redis.service.control;

import java.time.Duration;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

//@Slf4j
@Component
class SleeperBA {

    Mono<Long> execute(long sleepTime) {
        return Mono.delay(Duration.ofMillis(sleepTime));
    }
}
