package sia.finance_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.finance_tracker.entity.Transaction;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
}

