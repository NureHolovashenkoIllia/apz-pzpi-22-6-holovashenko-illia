package ua.nure.holovashenko.flameguard_api.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import ua.nure.holovashenko.flameguard_api.entity.UserAccount;
import ua.nure.holovashenko.flameguard_api.repository.UserAccountRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserAccountRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAccount user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getUserPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getUserRole()))
        );
    }
}
