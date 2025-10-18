package br.com.drazevedo.dealrooms.infrastructure.configuration;

import br.com.drazevedo.dealrooms.application.service.UserServiceImpl;
import br.com.drazevedo.dealrooms.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
public class SecurityConfiguration {

    /**
     * Define the password encoder.
     *
     * @return {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define the user service to retrieve and authenticate the user.
     *
     * @param userRepository
     * @return {@link UserDetails}
     */
    @Bean
    UserServiceImpl userServiceImpl(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    /**
     * Define the filer chain.
     *
     * @param http
     * @return {@link SecurityFilterChain}
     * @throws Exception
     */
    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        (authz) -> authz.requestMatchers("/secured").authenticated().anyRequest().permitAll())
                .httpBasic();
        return http.build();
    }

}
