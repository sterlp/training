package org.sterl.training.springquartz.example.api;

import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.sterl.training.springquartz.example.control.StoreJobControllerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store-jobs")
public class ExampleController {

    private final StoreJobControllerService jobControllerService;
    
    @Transactional
    @PostMapping("/notify-error")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public TriggerKey notifyUserWithError(@RequestBody(required = true) String user) throws SchedulerException {
        TriggerKey foo = jobControllerService.notifyUser(user);
        if (true == true) throw new RuntimeException("nope " + user);
        return foo;
    }

    @PostMapping("/notify")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public TriggerKey notifyUser(@RequestBody(required = true) String user) throws SchedulerException {
        return jobControllerService.notifyUser(user);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public TriggerKey createItems(@RequestBody(required = true) int count) throws SchedulerException {
        return jobControllerService.triggerRetryJob(count);
    }
    @DeleteMapping
    public int createItems() throws SchedulerException {
        return jobControllerService.cancelCreateItems();
    }

    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @PostMapping("/sleep/{id}/{group}")
    public TriggerKey triggerStoreSleepWithId(
            @RequestBody(required = false) int sleepTime,
            @PathVariable String id, @PathVariable String group) throws SchedulerException {
        if (sleepTime == 0) sleepTime = 1;
        return jobControllerService.createStoreSleepJob(sleepTime, id,  group);
    }

    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @PostMapping("/sleep")
    public TriggerKey triggerStoreSleep(@RequestBody(required = false) int sleepTime) throws SchedulerException {
        return this.triggerStoreSleepWithId(sleepTime, null, null);
    }
}
