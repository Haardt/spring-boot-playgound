package com.sft.config.security.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAnyRole('RESELLER', 'PBX') and principal.hasAccessOnCustomer(#customerId)")
public @interface OwnerAccess {

}
