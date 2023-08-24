package com.sft.config.security.annotations;

import com.sft.user.validator.UniqueUserNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@PreAuthorize("hasRole('RESELLER') and principal.hasAccessOnCustomer(#customerId)")
public @interface ResellerAccess {

}
