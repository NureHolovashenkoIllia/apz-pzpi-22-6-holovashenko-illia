package ua.nure.holovashenko.flameguard_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.nure.holovashenko.flameguard_api.dto.BuildingDto;
import ua.nure.holovashenko.flameguard_api.dto.UserAccountDto;
import ua.nure.holovashenko.flameguard_api.entity.Building;
import ua.nure.holovashenko.flameguard_api.entity.UserAccount;
import ua.nure.holovashenko.flameguard_api.repository.UserAccountRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }

    public UserAccount getUserAccountById(Integer id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    public UserAccount createUserAccount(UserAccount userAccount) {
        return userAccountRepository.save(userAccount);
    }

    public UserAccount updateUserAccount(Integer id, UserAccount userAccount) {
        UserAccount existingUserAccount = getUserAccountById(id);
        existingUserAccount.setFirstName(userAccount.getFirstName());
        existingUserAccount.setLastName(userAccount.getLastName());
        existingUserAccount.setPhoneNumber(userAccount.getPhoneNumber());
        existingUserAccount.setEmail(userAccount.getEmail());
        existingUserAccount.setUserPassword(userAccount.getUserPassword());
        existingUserAccount.setUserRole(userAccount.getUserRole());
        return userAccountRepository.save(existingUserAccount);
    }

    public UserAccountDto getProfile(UserDetails userDetails) {
        UserAccount user = userAccountRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userDetails.getUsername()));

        return convertToUserAccountDto(user);
    }

    public void deleteUserAccount(Integer id) {
        UserAccount userAccount = getUserAccountById(id);
        userAccountRepository.delete(userAccount);
    }

    public UserAccount registerUserAccount(UserAccount userAccount) {
        if (userAccountRepository.findByEmail(userAccount.getEmail()).isPresent()) {
            throw new RuntimeException("User account with email " + userAccount.getEmail() + " already exists");
        }

        userAccount.setUserPassword(passwordEncoder.encode(userAccount.getUserPassword()));
        return userAccountRepository.save(userAccount);
    }

    public UserAccount loginUser(String email, String password) {
        Optional<UserAccount> user = userAccountRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getUserPassword())) {
            return user.get();
        } else {
            throw new IllegalArgumentException("Invalid email or password.");
        }
    }

    public UserAccount updateUserRole(Integer userAccountId, String role) {
        UserAccount userAccount = getUserAccountById(userAccountId);

        if (!isValidUserRole(role)) {
            throw new RuntimeException("Invalid user role: " + role);
        }

        userAccount.setUserRole(role);
        return userAccountRepository.save(userAccount);
    }

    public UserAccount setDefaultUserRole(Integer userAccountId) {
        UserAccount userAccount = getUserAccountById(userAccountId);

        userAccount.setUserRole("customer");
        return userAccountRepository.save(userAccount);
    }

    public boolean isValidUserRole(String role) {
        return role.equals("customer") || role.equals("system_administrator") ||
                role.equals("database_admin") || role.equals("global_administrator");
    }

    private UserAccountDto convertToUserAccountDto(UserAccount user) {
        return new UserAccountDto(
                user.getUserAccountId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getUserPassword(),
                user.getUserRole()
        );
    }
}

