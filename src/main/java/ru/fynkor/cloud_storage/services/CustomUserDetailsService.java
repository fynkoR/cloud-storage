package ru.fynkor.cloud_storage.services;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.fynkor.cloud_storage.entities.User;
import ru.fynkor.cloud_storage.repositories.UserRepository;
import ru.fynkor.cloud_storage.security.CustomUserDetails;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);

        if(user.isEmpty()){
            throw new UsernameNotFoundException(username);
        }

        return new CustomUserDetails(user.get());
    }
}
