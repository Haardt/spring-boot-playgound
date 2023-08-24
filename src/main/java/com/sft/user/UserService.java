package com.sft.user;

import com.sft.config.security.users.AuthUser;
import com.sft.config.security.users.ResellerAdmin;
import com.sft.user.dto.UserDto;
import com.sft.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@Service
public class UserService {
    private final UserRepository userRepository;
    private final LocalValidatorFactoryBean validator;

    public UserDto saveUser(AuthUser authUser, @Valid UserDto userDto) {
//        val userDto2 = new UserDto(null, null, null, null, null, null);
        val result = validator.validate(userDto);
        if (! result.isEmpty()) {
            result.forEach(System.out::println);
            // TODO: Custom exception
            throw new IllegalArgumentException();
        }

        return userRepository.save(User.fromDto(userDto, userDto.customerId())).toUserDto();
    }

    public List<User> users(AuthUser user) {
        System.out.println("Service user customer:" + user.getCustomerId());
        return userRepository.findAll();
    }

    public void deleteUser(ResellerAdmin user, Long userId) {
        userRepository.deleteById(userId);
    }

    public List<User.NameOnly> usernames(AuthUser user) {
        return userRepository.findBy();
    }

    public Optional<User> findByUserId(AuthUser user, Long userId) {
        return userRepository.findById(userId);
    }
}
