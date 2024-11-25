package sia.finance_tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sia.finance_tracker.entity.User;
import sia.finance_tracker.security.JwtTokenUtil;
import sia.finance_tracker.service.UserService;
import sia.finance_tracker.security.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users", description = "Users that can register,login,logout or delete their account.")
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3069")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Register a new user.",
            description = "Register a new user object. The response is user object with its id,email and username.",
            tags = { "users", "post" })
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get a user by Id.",
            description = "Get a user object by Id. The response is user object with its id,email and username.",
            tags = { "users", "get" })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Update user information by Id.",
            description = "Update a user object by Id. The response is user object with its updated id,email and username.",
            tags = { "users", "put" })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = JwtTokenUtil.extractUsername(token);

            User currentUser = userService.getUserByUsername(username);
            if (currentUser != null && currentUser.getId().equals(id)) {
                updatedUser.setId(id);
                User user = userService.updateUser(id, updatedUser);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
    }


    @Operation(
            summary = "Delete a user by Id.",
            description = "Delete a user object by Id. The response is user object with its id,email and username being deleted.",
            tags = { "users", "delete" })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        // Get JWT token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extract token from "Bearer <token>"
            String username = JwtTokenUtil.extractUsername(token); // Extract username from token

            // Retrieve the user based on the username from the token
            User currentUser = userService.getUserByUsername(username);

            if (currentUser != null && currentUser.getId().equals(id)) {
                userService.deleteUser(id);
                return ResponseEntity.noContent().build(); // Successful deletion
            }
        }
        return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN); // Unauthorized
    }


    @Operation(
            summary = "Get all users.",
            description = "Get all user objects. The response is all user objects with its id,email and username.",
            tags = { "users", "get" })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Login feature
    @Operation(
            summary = "login a user.",
            description = "login a user object. The response is a user object with its id,email and username being able to access other endpoints.",
            tags = { "users", "post" })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userService.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // Generate JWT token
            //String token = JwtTokenUtil.generateToken(username);

            // Add userId to the token payload
            String tokenWithUserId = JwtTokenUtil.generateTokenWithUserId(user.getId(), username);

            // Log the generated token and its type
            logger.info("Generated JWT Token: {}", tokenWithUserId);
            logger.info("Type of token: {}", tokenWithUserId.getClass().getName());  // This will show the class type of the token

            // Return token in response (as a string)
//            return ResponseEntity.ok(token);

            // Return the token in the response
            return ResponseEntity.ok(tokenWithUserId); // Return token that includes userId
        }

        // Log failed login
        logger.warn("Login failed for username: {}", username);

        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }



    // Logout feature
    @Operation(
            summary = "logout a user.",
            description = "logout a user object. The response is a user object with its id,email and username not being able to access other endpoints.",
            tags = { "users", "post" })
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        request.getSession().invalidate();
        return new ResponseEntity<>("Logout successful", HttpStatus.OK);
    }
}






