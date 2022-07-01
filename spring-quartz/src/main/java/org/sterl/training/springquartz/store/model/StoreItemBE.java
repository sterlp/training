package org.sterl.training.springquartz.store.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "STORE_ITEM")
@Data
@EqualsAndHashCode(of = "id")
public class StoreItemBE {

    @GeneratedValue
    @Id
    private Long id;
    private String name;
    private BigDecimal price;
}
