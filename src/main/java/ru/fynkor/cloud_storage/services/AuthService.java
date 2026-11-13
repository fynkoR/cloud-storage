package ru.fynkor.cloud_storage.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import ru.fynkor.cloud_storage.dto.UserRequestDto;
import ru.fynkor.cloud_storage.security.CustomUserDetails;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextLogoutHandler securityContextLogoutHandler;

    public AuthService(AuthenticationManager authenticationManager, SecurityContextLogoutHandler securityContextLogoutHandler) {
        this.authenticationManager = authenticationManager;
        this.securityContextLogoutHandler = securityContextLogoutHandler;
    }

    public String performAuthentication(UserRequestDto userRequestDto){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userRequestDto.username(),
                        userRequestDto.password()
                );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUsername();
    }

    public void performLogout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ){
        securityContextLogoutHandler.logout(request, response, authentication);
    }
}
