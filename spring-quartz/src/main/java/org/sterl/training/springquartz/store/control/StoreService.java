package org.sterl.training.springquartz.store.control;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(timeout = 10)
public class StoreService {

}
