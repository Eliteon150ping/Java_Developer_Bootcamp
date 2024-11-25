package sia.finance_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.finance_tracker.entity.Goal;
import sia.finance_tracker.entity.User;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUser(User user); // Custom method to find goals by user
}


