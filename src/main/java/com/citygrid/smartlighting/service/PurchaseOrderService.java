package com.citygrid.smartlighting.service;

import com.citygrid.smartlighting.entity.OrderStatus;
import com.citygrid.smartlighting.entity.PurchaseOrder;
import com.citygrid.smartlighting.exception.ResourceNotFoundException;
import com.citygrid.smartlighting.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public PurchaseOrder create(PurchaseOrder po) {
        if (po.getOrderDate() == null) {
            po.setOrderDate(LocalDate.now());
        }
        po.setStatus(OrderStatus.CONFIRMED);
        return purchaseOrderRepository.save(po);
    }

    public List<PurchaseOrder> getAll() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder getById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found: " + id));
    }
}
