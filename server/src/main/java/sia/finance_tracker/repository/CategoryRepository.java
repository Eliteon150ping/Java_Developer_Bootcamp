package sia.finance_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sia.finance_tracker.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Custom query methods (if needed) can be added here
    // Example: Optional<Category.css> findByName(String name);
}

