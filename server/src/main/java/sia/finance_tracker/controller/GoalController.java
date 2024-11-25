//package sia.finance_tracker.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import sia.finance_tracker.entity.Goal;
//import sia.finance_tracker.entity.User;
//import sia.finance_tracker.service.GoalService;
//import sia.finance_tracker.service.UserService;
//import java.util.List;
//
//import io.swagger.v3.oas.annotations.tags.Tag;
//
//@Tag(name = "Goals", description = "Goals that show what item a user wants to buy with needed amount and what current amount they have.")
//@RestController
//@RequestMapping("/api/goals")
//public class GoalController {
//
//    private final GoalService goalService;
//    private final UserService userService;
//
//    public GoalController(GoalService goalService, UserService userService) {
//        this.goalService = goalService;
//        this.userService = userService;
//    }
//
//    @Operation(
//            summary = "Create a Goal.",
//            description = "Get a Goal object. The response is Goal object with its id and name.",
//            tags = { "Goal", "post" })
//    @PostMapping
//    public ResponseEntity<Goal> createGoal(@RequestBody Goal goal, HttpServletRequest request) {
//        User user = (User) request.getSession().getAttribute("user");
//        if (user == null) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//        goal.setUser(user);
//        Goal createdGoal = goalService.createGoal(goal);
//        return new ResponseEntity<>(createdGoal, HttpStatus.CREATED); // Returns the entire Goal object, including the id
//    }
//
//    @Operation(
//            summary = "Update a Goal.",
//            description = "Update a Goal object. The response is Goal object with its id and name.",
//            tags = { "Goal", "put" })
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateGoal(@PathVariable Long id, @RequestBody Goal updatedGoal, HttpServletRequest request) {
//        User user = (User) request.getSession().getAttribute("user");
//        if (user == null) {
//            return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
//        }
//        Goal existingGoal = goalService.getGoalById(id);
//        if (existingGoal.getUser().getId().equals(user.getId())) {
//            updatedGoal.setUser(user);
//            Goal goal = goalService.updateGoal(id, updatedGoal);
//            return new ResponseEntity<>(goal, HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
//    }
//
//    @Operation(
//            summary = "Delete a Goal.",
//            description = "Delete a Goal object. The response is Goal object with its id and name being deleted.",
//            tags = { "Goal", "delete" })
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteGoal(@PathVariable Long id, HttpServletRequest request) {
//        User user = (User) request.getSession().getAttribute("user");
//        if (user == null) {
//            return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
//        }
//        Goal goal = goalService.getGoalById(id);
//        if (goal.getUser().getId().equals(user.getId())) {
//            goalService.deleteGoal(id);
//            return ResponseEntity.noContent().build();
//        }
//        return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
//    }
//
//    @Operation(
//            summary = "Get all Goals.",
//            description = "Get all Goal objects. The response is Goal object with its id and name.",
//            tags = { "Goal", "Get" })
//    @GetMapping
//    public ResponseEntity<?> getGoals(HttpServletRequest request) {
//        User user = (User) request.getSession().getAttribute("user");
//        if (user == null) {
//            return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
//        }
//        List<Goal> goals = goalService.getGoalsByUser(user);
//        return ResponseEntity.ok(goals);
//    }
//}

package sia.finance_tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sia.finance_tracker.entity.Goal;
import sia.finance_tracker.entity.User;
import sia.finance_tracker.service.GoalService;
import sia.finance_tracker.service.UserService;
import sia.finance_tracker.security.JwtTokenUtil; // Assuming you have this utility for JWT parsing

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Goals", description = "Goals that show what item a user wants to buy with needed amount and what current amount they have.")
@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil; // Utility for handling JWT tokens

    public GoalController(GoalService goalService, UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.goalService = goalService;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Operation(
            summary = "Create a Goal.",
            description = "Get a Goal object. The response is Goal object with its id and name.",
            tags = { "Goal", "post" })
    @PostMapping
    public ResponseEntity<Goal> createGoal(@RequestBody Goal goal, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Extract the token from the Authorization header
        String username = jwtTokenUtil.extractUsername(token); // Extract username from token

        if (username == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        goal.setUser(user);
        Goal createdGoal = goalService.createGoal(goal);
        return new ResponseEntity<>(createdGoal, HttpStatus.CREATED); // Returns the entire Goal object, including the id
    }

    @Operation(
            summary = "Update a Goal.",
            description = "Update a Goal object. The response is Goal object with its id and name.",
            tags = { "Goal", "put" })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGoal(@PathVariable Long id, @RequestBody Goal updatedGoal, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Extract the token from the Authorization header
        String username = jwtTokenUtil.extractUsername(token); // Extract username from token

        if (username == null) {
            return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);
        }

        Goal existingGoal = goalService.getGoalById(id);
        if (existingGoal.getUser().getId().equals(user.getId())) {
            updatedGoal.setUser(user);
            Goal goal = goalService.updateGoal(id, updatedGoal);
            return new ResponseEntity<>(goal, HttpStatus.OK);
        }
        return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
    }

    @Operation(
            summary = "Delete a Goal.",
            description = "Delete a Goal object. The response is Goal object with its id and name being deleted.",
            tags = { "Goal", "delete" })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Extract the token from the Authorization header
        String username = jwtTokenUtil.extractUsername(token); // Extract username from token

        if (username == null) {
            return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);
        }

        Goal goal = goalService.getGoalById(id);
        if (goal.getUser().getId().equals(user.getId())) {
            goalService.deleteGoal(id);
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
    }

    @Operation(
            summary = "Get all Goals.",
            description = "Get all Goal objects. The response is Goal object with its id and name.",
            tags = { "Goal", "Get" })
    @GetMapping
    public ResponseEntity<?> getGoals(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Extract the token from the Authorization header
        String username = jwtTokenUtil.extractUsername(token); // Extract username from token

        if (username == null) {
            return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);
        }

        List<Goal> goals = goalService.getGoalsByUser(user);
        return ResponseEntity.ok(goals);
    }
}
