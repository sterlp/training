package org.sterl.training.hateoas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class ValidatedBean {
    @GeneratedValue
    @Id @Column(updatable = false)
    private Long id;

    @NotNull @Size(min = 2, max = 1024)
    private String name;
}
