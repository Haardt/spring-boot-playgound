package com.sft.config.startup;

import com.sft.config.security.AuthenticationFacade;
import com.sft.config.security.TenantIdentifierResolver;
import com.sft.user.User;
import com.sft.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
public class UserDefaults implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final TenantIdentifierResolver tenantIdentifierResolver;

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
        tenantIdentifierResolver.setCurrentCustomerId("1000");
        var admin = userRepository.findByLastName("admin")
                        .orElseGet(() -> userRepository.save(
                                new User(
                                null,
                                        "admin",
                                        "admin",
                                        "admin",
                                        "email",
                                        1000L)
                        ));
        System.out.println("Default user enabled:" + admin.getLastName());
        tenantIdentifierResolver.setCurrentCustomerId("1001");
        var user = userRepository.findByLastName("user")
                        .orElseGet(() -> userRepository.save(
                                new User(
                                null,
                                        "user",
                                        "user",
                                        "user",
                                        "email",
                                        1001L)
                        ));
        System.out.println("Default user enabled:" + user.getLastName());
        tenantIdentifierResolver.setCurrentCustomerId("1002");
        var user1 = userRepository.findByLastName("user1")
                        .orElseGet(() -> userRepository.save(
                                new User(
                                null,
                                        "user1",
                                        "user1",
                                        "user",
                                        "email",
                                        1002L)
                        ));
        System.out.println("Default user enabled:" + user1.getLastName());
    }
}
