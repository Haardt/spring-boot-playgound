package com.sft.config.security.users;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.function.Consumer;

@FunctionalInterface
public interface AuthUserFactory<T extends AuthUser> {
    T create(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Long customerId, Consumer<String> setCurrentConsumerId);
}
