package io.mavg.challenge.repository.transactionallog;

import io.mavg.challenge.domain.entities.TransactionalLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionalLogRepository extends JpaRepository<TransactionalLog, Long> {
}
