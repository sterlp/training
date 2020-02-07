package org.sterl.training.hateoas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "mybean")
public interface ValidatedBeanDao extends JpaRepository<ValidatedBean, Long> {

}
