package com.citygrid.smartlighting.repository;

import com.citygrid.smartlighting.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
