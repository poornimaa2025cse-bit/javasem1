package com.citygrid.smartlighting.repository;

import com.citygrid.smartlighting.entity.AnalyticAccount;
import com.citygrid.smartlighting.entity.BillStatus;
import com.citygrid.smartlighting.entity.VendorBill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface VendorBillRepository extends JpaRepository<VendorBill, Long> {
    List<VendorBill> findByStatus(BillStatus status);
    List<VendorBill> findByAnalyticAccountAndStatusAndBillDateBetween(
            AnalyticAccount analyticAccount, BillStatus status, LocalDate start, LocalDate end);
}
