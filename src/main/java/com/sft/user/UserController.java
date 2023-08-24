package com.sft.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.sft.config.security.annotations.ResellerAccess;
import com.sft.config.security.users.AuthUser;
import com.sft.config.security.users.CustomerAdmin;
import com.sft.config.security.users.ResellerAdmin;
import com.sft.user.dto.UserDto;
import com.sft.user.dto.UserProjection;
import com.sft.user.dto.UserViews;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/{customerId}/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('RESELLER', 'PBX') and principal.hasAccessOnCustomer(#customerId)")
    public MappingJacksonValue all(@AuthenticationPrincipal AuthUser user, @NotNull @PathVariable Long customerId,
                             @RequestParam(required = false, defaultValue = "NO_PROJECTION") String projection) {
        return switch (UserProjection.valueOf(projection.toUpperCase())) {
            case USERNAMES -> {
                MappingJacksonValue value = new MappingJacksonValue(User.toUsernamesDto(userService.usernames(user)));
                value.setSerializationView(UserViews.Usernames.class);
                yield value;
            }
            case NO_PROJECTION -> {
                MappingJacksonValue value = new MappingJacksonValue(User.toDto(userService.users(user)));
                value.setSerializationView(UserViews.Edit.class);
                yield value;
            }
        };
    }
    @GetMapping("/{userId}")
    @JsonView(UserViews.Edit.class)
    @PreAuthorize("isAuthenticated() and principal.hasAccessOnCustomer(#customerId)")
    public ResponseEntity<UserDto> getUser(@AuthenticationPrincipal AuthUser user,
                                  @NotNull @PathVariable Long customerId,
                                           @NotNull @PathVariable Long userId) {
        return userService.findByUserId(user, userId)
                .map(User::toUserDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    @JsonView(UserViews.Create.class)
    @PreAuthorize("isAuthenticated() and principal.hasAccessOnCustomer(#customerId)")
    public UserDto createUser(@AuthenticationPrincipal AuthUser user,
                              @NotNull @PathVariable Long customerId,
                              @Valid @RequestBody UserDto userDto) {
        return userService.saveUser(user, userDto.withCustomerId(customerId));
    }

    @DeleteMapping("/{userId}")
    @ResellerAccess
    public void deleteUser(@AuthenticationPrincipal AuthUser user,
                           @NotNull @PathVariable Long customerId, @NotNull @PathVariable Long userId) {
        switch (user) {
            case ResellerAdmin resellerAdmin -> {
                userService.deleteUser(resellerAdmin, userId);
            }
            case CustomerAdmin customerAdmin -> {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Access denied", null);
            }
        }
    }
}
