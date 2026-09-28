package com.citygrid.smartlighting.repository;

import com.citygrid.smartlighting.entity.Account;
import com.citygrid.smartlighting.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByType(AccountType type);
    Optional<Account> findByCode(String code);
}
