package com.sft.user.repository;


import com.sft.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLastName(String admin);

    boolean existsByLastName(String lastName);

    List<User.NameOnly> findBy();
}
