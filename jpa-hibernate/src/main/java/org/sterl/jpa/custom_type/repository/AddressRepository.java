package org.sterl.jpa.custom_type.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.sterl.jpa.custom_type.model.Address;
import org.sterl.jpa.custom_type.model.Zip;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByZip(Zip zip);
    
    @Query("""
            SELECT e FROM Address e
            WHERE CAST(e.zip AS string) like :zip
            ORDER BY ID
            """)
    List<Address> search(String zip);
}
