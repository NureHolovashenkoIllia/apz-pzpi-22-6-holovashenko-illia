package ua.nure.holovashenko.flameguard_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ua.nure.holovashenko.flameguard_api.entity.UserAccount;
import ua.nure.holovashenko.flameguard_api.service.UserAccountService;
import ua.nure.holovashenko.flameguard_api.util.JWTUtil;

import java.util.List;

/**
 * REST controller for authorisation management.
 * Provides endpoints for user authentication and registration.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * Registers a new user account and generates a JWT token.
     *
     * @param userAccount The user account details for registration.
     * @return A response containing the generated JWT token or an error message.
     */
    @Operation(summary = "Register a new user account", description = "Registers a new user and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid user account details",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"error\": \"Invalid user data\"}")))
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserAccount userAccount) {
        try {
            UserAccount user = userAccountService.registerUserAccount(userAccount);
            String token = jwtUtil.generateToken(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("{ \"token\":\"" + token + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{ \"error\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Logs in a user and generates a JWT token.
     *
     * @param email    The email address of the user.
     * @param password The password of the user.
     * @return A response containing the generated JWT token or an error message.
     */
    @Operation(summary = "Login a user", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged in",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"error\": \"Invalid credentials\"}")))
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        try {
            UserAccount user = userAccountService.loginUser(email, password);
            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok("{ \"token\":\"" + token + "\",\n \"role\":\"" + user.getUserRole() + "\",\n \"id\":\"" + user.getUserAccountId() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Operation(summary = "Отримання профілю авторизованого користувача")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профіль знайдено"),
            @ApiResponse(responseCode = "404", description = "Користувача не знайдено або відсутні розширені дані"),
            @ApiResponse(responseCode = "400", description = "Невідома роль користувача"),
            @ApiResponse(responseCode = "401", description = "Неавторизований запит")
    })
    @GetMapping("/profile")
    public ResponseEntity<?> profile(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(userAccountService.getProfile(userDetails));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("{\"Помилка при отриманні профілю\":\"" + e.getMessage() + "\"}");
        }
    }
}