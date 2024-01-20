package org.sterl.jpa.embeddable.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.sterl.jpa.embeddable.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("""
           SELECT e FROM Person e
           WHERE e.address.zip.value like :zip
           """)
    List<Person> findByZip(String zip);

}
