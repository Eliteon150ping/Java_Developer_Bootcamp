package sia.finance_tracker.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import sia.finance_tracker.entity.Goal;
import sia.finance_tracker.entity.User;
import sia.finance_tracker.repository.GoalRepository;

import java.util.List;

@Service
public class GoalService {

    private final GoalRepository goalRepository;

    @Autowired
    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public Goal createGoal(Goal goal) {
        return goalRepository.save(goal);
    }

    public Goal getGoalById(Long id) {
        return goalRepository.findById(id).orElseThrow(() -> new RuntimeException("Goal not found"));
    }

    public Goal updateGoal(Long id, Goal updatedGoal) {
        Goal existingGoal = getGoalById(id);
        existingGoal.setDescription(updatedGoal.getDescription());
        existingGoal.setTargetAmount(updatedGoal.getTargetAmount());
        existingGoal.setCurrentAmount(updatedGoal.getCurrentAmount());
        existingGoal.setTargetDate(updatedGoal.getTargetDate());
        return goalRepository.save(existingGoal);
    }

    public void deleteGoal(Long id) {
        goalRepository.deleteById(id);
    }

    public List<Goal> getAllGoals() {
        return goalRepository.findAll();
    }

    public List<Goal> getGoalsByUser(User user) {
        return goalRepository.findByUser(user); // Retrieves goals linked to the specific user
    }

}
