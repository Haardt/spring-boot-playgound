package com.sft.config.security.users;

import jakarta.annotation.security.RunAs;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface AuthUser extends UserDetails permits ResellerAdmin, CustomerAdmin {

    public static AuthUserBuilder withLastName(String username) {
        return Builder().username(username);
    }

    public static AuthUserBuilder Builder() {
        return new AuthUserBuilder();
    }

    Long getCustomerId();

    boolean hasAccessOnCustomer(Long customerId);

    public static final class AuthUserBuilder {

        private String username;

        private String password;

        private List<GrantedAuthority> authorities = new ArrayList<>();
        private boolean accountExpired;

        private boolean accountLocked;

        private boolean credentialsExpired;

        private boolean disabled;

        private Long customerId;

        private Function<String, String> passwordEncoder = (password) -> password;
        private Consumer<String> setCurrentCustomerId;

        /**
         * Creates a new instance
         */
        private AuthUserBuilder() {
        }

        public AuthUser.AuthUserBuilder username(String username) {
            Assert.notNull(username, "username cannot be null");
            this.username = username;
            return this;
        }

        public AuthUser.AuthUserBuilder password(String password) {
            Assert.notNull(password, "password cannot be null");
            this.password = password;
            return this;
        }

        public AuthUser.AuthUserBuilder passwordEncoder(Function<String, String> encoder) {
            Assert.notNull(encoder, "encoder cannot be null");
            this.passwordEncoder = encoder;
            return this;
        }

        public AuthUser.AuthUserBuilder roles(String... roles) {
            List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
            for (String role : roles) {
                Assert.isTrue(!role.startsWith("ROLE_"),
                        () -> role + " cannot start with ROLE_ (it is automatically added)");
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
            return authorities(authorities);
        }

        public AuthUser.AuthUserBuilder authorities(GrantedAuthority... authorities) {
            Assert.notNull(authorities, "authorities cannot be null");
            return authorities(Arrays.asList(authorities));
        }

        public AuthUser.AuthUserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            Assert.notNull(authorities, "authorities cannot be null");
            this.authorities = new ArrayList<>(authorities);
            return this;
        }

        public AuthUser.AuthUserBuilder authorities(String... authorities) {
            Assert.notNull(authorities, "authorities cannot be null");
            return authorities(AuthorityUtils.createAuthorityList(authorities));
        }

        public AuthUser.AuthUserBuilder accountExpired(boolean accountExpired) {
            this.accountExpired = accountExpired;
            return this;
        }

        public AuthUser.AuthUserBuilder accountLocked(boolean accountLocked) {
            this.accountLocked = accountLocked;
            return this;
        }

        public AuthUser.AuthUserBuilder credentialsExpired(boolean credentialsExpired) {
            this.credentialsExpired = credentialsExpired;
            return this;
        }

        public AuthUser.AuthUserBuilder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public AuthUser.AuthUserBuilder customerId(Long customerId) {
            Assert.notNull(customerId, "customerId cannot be null");
            this.customerId = customerId;
            return this;
        }

        public <T  extends AuthUser> UserDetails build(AuthUserFactory<T> factory) {
            String encodedPassword = this.passwordEncoder.apply(this.password);
            return factory.create(this.username, encodedPassword, !this.disabled, !this.accountExpired,
                    !this.credentialsExpired, !this.accountLocked, this.authorities, this.customerId, this.setCurrentCustomerId);
        }

        public AuthUserBuilder updateCurrentReseller(Consumer<String> setCurrentCustomerId) {
            this.setCurrentCustomerId = setCurrentCustomerId;
            return this;
        }
    }

}
