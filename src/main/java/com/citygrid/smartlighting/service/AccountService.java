package com.citygrid.smartlighting.service;

import com.citygrid.smartlighting.entity.Account;
import com.citygrid.smartlighting.exception.ResourceNotFoundException;
import com.citygrid.smartlighting.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account create(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account getById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + id));
    }

    public Account getByCode(String code) {
        return accountRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account with code " + code + " not found - has DataSeeder run?"));
    }
}
