package com.sft.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.sft.user.validator.UniqueUserNameValidation;
import jakarta.validation.constraints.NotNull;
import lombok.With;

import static com.sft.user.dto.UserViews.*;

@With
public record UserDto(

        @JsonView({Edit.class, Usernames.class})
        Long id,
        @JsonView({Create.class, Edit.class, Usernames.class})
        @UniqueUserNameValidation String lastName,
        @JsonView({Create.class, Edit.class})
        String firstName,
        @JsonView({Create.class})
        String password,
        @JsonView({Create.class, Edit.class})
        @NotNull
        String email,
        @JsonIgnore
        Long customerId) {
}
