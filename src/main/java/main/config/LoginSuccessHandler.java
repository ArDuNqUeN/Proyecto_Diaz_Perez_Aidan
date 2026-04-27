package main.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        // Comparamos con las authorities que tiene el usuario autenticado
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            response.sendRedirect("/panel/admin");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ALUMNO"))) {
            response.sendRedirect("/panel/alumno");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TUTOR_EMPRESA"))) {
            response.sendRedirect("/panel/tutor-empresa");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TUTOR_CENTRO"))) {
            response.sendRedirect("/panel/tutor-centro");
        } else {
            // Si no coincide ningún rol, lo mandamos al login con error
            response.sendRedirect("/login?error");
        }
    }
}