package com.citygrid.smartlighting.repository;

import com.citygrid.smartlighting.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
}
