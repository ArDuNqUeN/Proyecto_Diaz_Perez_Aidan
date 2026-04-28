package main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;

    public SecurityConfig(LoginSuccessHandler loginSuccessHandler) {
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas (acceso sin login)
                .requestMatchers("/", "/index", "/login", "/css/**", "/img/**", "/js/**", "/h2-console/**").permitAll()
                
                // Rutas por rol - Fíjate que coinciden con tus @RequestMapping
                .requestMatchers("/panel/admin/**").hasRole("ADMIN")
                .requestMatchers("/panel/alumno/**").hasRole("ALUMNO")
                .requestMatchers("/panel/tutor-empresa/**").hasRole("TUTOR_EMPRESA")
                .requestMatchers("/panel/tutor-centro/**").hasRole("TUTOR_CENTRO")
                
                // El resto requiere estar autenticado
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(loginSuccessHandler)  // Tu manejador personalizado
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
            )
            .csrf(csrf -> csrf
            	    .ignoringRequestMatchers("/h2-console/**")
            	)
            	.headers(headers -> headers
            	    .frameOptions(frame -> frame.sameOrigin())
            	);

        
        return http.build();
    }
}