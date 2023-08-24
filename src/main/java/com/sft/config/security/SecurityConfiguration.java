package com.sft.config.security;

import com.sft.config.security.users.AuthUser;
import com.sft.config.security.users.CustomerAdmin;
import com.sft.config.security.users.ResellerAdmin;
import com.sft.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AnonymousConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@EnableWebSecurity(debug = false)
@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration {

    private final UserRepository userRepository;
    private final TenantIdentifierResolver tenantIdentifierResolver;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(CorsConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .anonymous(AnonymousConfigurer::disable)
                .formLogin(config -> {
                    config.usernameParameter("username");
                    config.defaultSuccessUrl("/api/customer/1000/users/");
                })
                .sessionManagement(config -> {
                   config.sessionFixation().changeSessionId();
                });
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.contentEquals(rawPassword);
            }
        };
    }

    @Bean
    public UserDetailsService userDetailService() {
        //TODO: Unsafe!
        var encoder = passwordEncoder();
        return username -> {
            // TODO: Unsafe?!
            tenantIdentifierResolver.setRoot(true);
            var authUser = userRepository.findByLastName(username)
                    .map(user ->
                            user.getLastName().equals("admin") ?
                                    AuthUser.withLastName(user.getLastName())
                                            .password(user.getPassword())
                                            .accountExpired(false)
                                            .accountLocked(false)
                                            .disabled(false)
                                            .customerId(user.getCustomerId())
                                            .authorities(new SimpleGrantedAuthority("ROLE_RESELLER"))
                                            .passwordEncoder(encoder::encode)
                                            .updateCurrentReseller(tenantIdentifierResolver::setCurrentCustomerId)
                                            .build(ResellerAdmin::new)
                                    :
                                    AuthUser.withLastName(user.getLastName())
                                            .password(user.getPassword())
                                            .accountExpired(false)
                                            .accountLocked(false)
                                            .disabled(false)
                                            .customerId(user.getCustomerId())
                                            .authorities(new SimpleGrantedAuthority("ROLE_PBX"))
                                            .passwordEncoder(encoder::encode)
                                            .build(CustomerAdmin::new)
                    )
                    .orElseThrow(() -> new UsernameNotFoundException("Unknown user"));
            tenantIdentifierResolver.setRoot(false);
            return authUser;
        };
    }
}