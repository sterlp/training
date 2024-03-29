package org.sterl.training.springquartz.example.control;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sterl.training.common.aspect.LogMethod;
import org.sterl.training.springquartz.example.model.StoreItemBE;
import org.sterl.training.springquartz.example.repository.StoreItemRepository;

@Service
@Transactional(timeout = 10)
public class StoreItemService {

    @Autowired StoreItemRepository repository;

    @LogMethod
    public void createItems(int count) {
        System.err.println(Thread.currentThread().getId() + " active ...");
        final Instant now = Instant.now();
        for (int i = 0; i < count; i++) {
            repository.save(StoreItemBE.builder()
                    .name(i + 1 + " - " + now)
                    .price(new BigDecimal(11.2 + i))
                    .build()
                );
            // just to demonstrate the TRX rollback
        }
        // just to slow it down a bit
        try {
            Thread.sleep(5250);
        } catch (InterruptedException e) {}

        if (count > 10) {
            System.err.println(Thread.currentThread().getId() + "... done.");
            throw new IllegalArgumentException("Count should not be greater than 10");
        }
        
        System.err.println(Thread.currentThread().getId() + "... done.");
    }

}
