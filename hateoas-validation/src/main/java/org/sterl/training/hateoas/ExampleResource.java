package org.sterl.training.hateoas;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ExampleResource {

    @PostMapping("api/validate")
    ResponseEntity<ValidatedBean> validate(@RequestBody @NotNull @Valid ValidatedBean e) {
        return ResponseEntity.ok(e);
    }
}
