package com.citygrid.smartlighting.service;

import com.citygrid.smartlighting.entity.AnalyticAccount;
import com.citygrid.smartlighting.exception.ResourceNotFoundException;
import com.citygrid.smartlighting.repository.AnalyticAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticAccountService {

    private final AnalyticAccountRepository analyticAccountRepository;

    public AnalyticAccountService(AnalyticAccountRepository analyticAccountRepository) {
        this.analyticAccountRepository = analyticAccountRepository;
    }

    public AnalyticAccount create(AnalyticAccount analyticAccount) {
        return analyticAccountRepository.save(analyticAccount);
    }

    public List<AnalyticAccount> getAll() {
        return analyticAccountRepository.findAll();
    }

    public AnalyticAccount getById(Long id) {
        return analyticAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found: " + id));
    }
}
