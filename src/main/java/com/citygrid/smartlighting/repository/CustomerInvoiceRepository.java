package com.citygrid.smartlighting.repository;

import com.citygrid.smartlighting.entity.BillStatus;
import com.citygrid.smartlighting.entity.CustomerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerInvoiceRepository extends JpaRepository<CustomerInvoice, Long> {
    List<CustomerInvoice> findByStatus(BillStatus status);
}
