package com.citygrid.smartlighting.repository;

import com.citygrid.smartlighting.entity.Account;
import com.citygrid.smartlighting.entity.JournalEntryLine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Long> {
    List<JournalEntryLine> findByAccount(Account account);
}
