package com.citygrid.smartlighting.repository;

import com.citygrid.smartlighting.entity.AnalyticAccount;
import com.citygrid.smartlighting.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByAnalyticAccount(AnalyticAccount analyticAccount);
}
