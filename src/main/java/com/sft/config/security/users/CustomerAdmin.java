package com.sft.config.security.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.function.Consumer;


public final class CustomerAdmin extends User implements AuthUser {

    private final Long customerId;

    public CustomerAdmin(String username, String password, Collection<? extends GrantedAuthority> authorities, Long customerId,
                         Consumer<String> setCurrentCustomerId) {
        super(username, password, authorities);
        this.customerId = customerId;
        setCurrentCustomerId.accept(String.valueOf(customerId));
    }

    public CustomerAdmin(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Long customerId, Consumer<String> setCurrentCustomerId) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.customerId = customerId;
        setCurrentCustomerId.accept(String.valueOf(customerId));
    }

    public Long getCustomerId() {
        return customerId;
    }

    @Override
    public boolean hasAccessOnCustomer(Long customerId) {
        return this.customerId.equals(customerId);
    }
}
