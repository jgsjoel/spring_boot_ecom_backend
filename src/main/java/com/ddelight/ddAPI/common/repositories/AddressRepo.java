package com.ddelight.ddAPI.common.repositories;

import com.ddelight.ddAPI.common.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address,Long> {

    Optional<Address> findByUserEmail(String email);

}
