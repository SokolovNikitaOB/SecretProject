package org.example.repository;

import org.example.domain.LogRecords;
import org.springframework.data.repository.CrudRepository;

public interface LogRecordsRepo extends CrudRepository<LogRecords, Long> {
}
