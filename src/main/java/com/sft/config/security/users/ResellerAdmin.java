package com.sft.config.security.users;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;


public final class ResellerAdmin extends User implements AuthUser {

    @Getter
    private Long customerId;
    private final Consumer<String> setCurrentCustomerId;

    public ResellerAdmin(String username, String password, Collection<? extends GrantedAuthority> authorities, Long customerId,
            Consumer<String> setCurrentCustomerId) {
        super(username, password, authorities);
        this.customerId = customerId;
        this.setCurrentCustomerId = setCurrentCustomerId;
    }

    public ResellerAdmin(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Long customerId, Consumer<String> setCurrentCustomerId) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.customerId = customerId;
        this.setCurrentCustomerId = setCurrentCustomerId;
    }

    List<Long> customers = List.of(1000L, 1001L);
    @Override
    public boolean hasAccessOnCustomer(Long customerId) {
        var hasAccess = customers.contains(customerId);
        if (hasAccess) {
            System.out.println("Has access on customer: " + customerId);
            this.setCurrentCustomerId.accept(String.valueOf(customerId));
            this.customerId = customerId;
        }
        return hasAccess;
    }
}
