package com.sft.config.security;

import com.sft.config.security.users.AuthUser;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AuthenticationFacade {

    @Setter
    private Long tempCurrentCustomerId = -1L;

    public Long getCustomerId() {

        val authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            System.out.println("No aut...use dummy.");
            return tempCurrentCustomerId;
        }
        // Kommt einfach zu frueh!

        System.out.println("Use customerId: " + ((AuthUser) authentication.getPrincipal()).getCustomerId());
        return ((AuthUser) authentication.getPrincipal()).getCustomerId();
    }
}