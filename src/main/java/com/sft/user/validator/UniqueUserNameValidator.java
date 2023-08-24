package com.sft.user.validator;

import com.sft.user.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserNameValidation, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String lastName, ConstraintValidatorContext context) {
        System.out.println("Check unique name:" + lastName);
        return !userRepository.existsByLastName(lastName);
    }
}
