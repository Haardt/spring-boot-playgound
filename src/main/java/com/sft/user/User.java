package com.sft.user;

import com.sft.user.dto.UserDto;
import com.sft.user.repository.UserRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TenantId;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {

    public interface NameOnly {
        Long getId();
        @Value("#{target.firstName + ' ' + target.lastName}")
        String getName();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "customerId")
    @TenantId
    private Long customerId;


    public static User fromDto(UserDto userDto, long customerId) {
        return new User(null, userDto.firstName(), userDto.lastName(), userDto.password(), userDto.email(), customerId);
    }

    public static List<UserDto> toUsernamesDto(List<User.NameOnly> usernames) {
        return usernames.stream().map(names -> new UserDto(names.getId(), names.getName(), null, null, null, null)).toList();
    }

    public static List<UserDto> toDto(List<User> users) {
        return users.stream().map(User::toUserDto).toList();
    }

    public UserDto toUserDto() {
        return new UserDto(id, firstName, lastName, email, password, customerId);
    }
}
