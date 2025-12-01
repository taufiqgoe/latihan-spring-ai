package id.taufiq.latihan.spring_ai.service;

import id.taufiq.latihan.spring_ai.model.entity.UserAccount;
import id.taufiq.latihan.spring_ai.repository.UserAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    public DatabaseUserDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        String[] roles = account.getRoles() == null ? new String[]{} : account.getRoles().split(",");
        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .roles(Arrays.stream(roles).map(String::trim).filter(r -> !r.isEmpty()).toArray(String[]::new))
                .disabled(!account.isEnabled())
                .build();
    }
}
