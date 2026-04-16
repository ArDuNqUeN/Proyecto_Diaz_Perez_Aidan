package main.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
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

        authentication.getAuthorities().forEach(auth -> {
            try {
                if (auth.getAuthority().equals("ROLE_ADMIN")) {
                    response.sendRedirect("/admin/dashboard");
                } else if (auth.getAuthority().equals("ROLE_ALUMNO")) {
                    response.sendRedirect("/alumno/home");
                } else {
                    response.sendRedirect("/login");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}