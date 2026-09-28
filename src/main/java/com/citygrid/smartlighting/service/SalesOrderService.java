package com.citygrid.smartlighting.service;

import com.citygrid.smartlighting.entity.OrderStatus;
import com.citygrid.smartlighting.entity.SalesOrder;
import com.citygrid.smartlighting.exception.ResourceNotFoundException;
import com.citygrid.smartlighting.repository.SalesOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;

    public SalesOrderService(SalesOrderRepository salesOrderRepository) {
        this.salesOrderRepository = salesOrderRepository;
    }

    public SalesOrder create(SalesOrder so) {
        if (so.getOrderDate() == null) {
            so.setOrderDate(LocalDate.now());
        }
        so.setStatus(OrderStatus.CONFIRMED);
        return salesOrderRepository.save(so);
    }

    public List<SalesOrder> getAll() {
        return salesOrderRepository.findAll();
    }

    public SalesOrder getById(Long id) {
        return salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales order not found: " + id));
    }
}
