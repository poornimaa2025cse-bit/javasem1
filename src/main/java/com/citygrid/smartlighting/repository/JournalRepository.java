package com.citygrid.smartlighting.repository;

import com.citygrid.smartlighting.entity.Journal;
import com.citygrid.smartlighting.entity.JournalType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JournalRepository extends JpaRepository<Journal, Long> {
    Optional<Journal> findByType(JournalType type);
}
