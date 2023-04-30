package com.junhyeong.chatchat.repositories;

import com.junhyeong.chatchat.models.user.User;
import com.junhyeong.chatchat.models.user.UserName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(UserName userName);
}
