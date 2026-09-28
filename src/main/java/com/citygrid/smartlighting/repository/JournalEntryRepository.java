package com.citygrid.smartlighting.repository;

import com.citygrid.smartlighting.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
}
