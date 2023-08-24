package com.sft.config.security;

import com.sft.config.security.users.AuthUser;
import com.sft.config.security.users.CustomerAdmin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Map;
@RequiredArgsConstructor
@Component
//@RequestScope
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver, HibernatePropertiesCustomizer {

    private final AuthenticationFacade authenticationFacade;


    private final ThreadLocal<String> threadLocalCustomerId = ThreadLocal.withInitial(() -> "BOOTSTRAP");
    private boolean root;

    public void setCurrentCustomerId(String currentCustomerId) {
        System.out.println("Set new customerId: " + currentCustomerId);
        this.threadLocalCustomerId.set(currentCustomerId);
    }

    @Override
    public @NotNull String resolveCurrentTenantIdentifier() {
        return this.threadLocalCustomerId.get();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    @Override
    public boolean isRoot(String tenantId) {
        return this.root;
    }
}