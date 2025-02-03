package com.ddelight.ddAPI.common.repositories;

import com.ddelight.ddAPI.common.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepo extends JpaRepository<Invoice,Long> {
}
