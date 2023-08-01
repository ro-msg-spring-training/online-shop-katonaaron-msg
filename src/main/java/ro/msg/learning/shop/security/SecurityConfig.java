package ro.msg.learning.shop.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           @Value("${ro.msg.learning.shop.security.authenticationType}")
                                           AuthenticationType authenticationType) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().authenticated()
                );

        switch (authenticationType) {
            case HTTP_BASIC -> setupHTTPBasicAuth(http);
            case FORM -> setupFormBasedAuth(http);
            default -> throw new IllegalStateException("Unexpected value: " + authenticationType);
        }
        return http.build();
    }

    private void setupFormBasedAuth(HttpSecurity http) throws Exception {
        http
                .formLogin(withDefaults())
                .logout(withDefaults());
    }

    private void setupHTTPBasicAuth(HttpSecurity http) throws Exception {
        http
                .httpBasic(withDefaults());

    }

    @Bean
    public DaoAuthenticationProvider getDaoAuthProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }


}
