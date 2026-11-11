package ru.fynkor.cloud_storage.services;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fynkor.cloud_storage.entities.User;
import ru.fynkor.cloud_storage.security.CustomUserDetails;

@Service
public class UserService {
    public String getUsernameByAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getUsername();
    }
}
