package com.junhyeong.chatchat.repositories;

import com.junhyeong.chatchat.models.token.Token;
import com.junhyeong.chatchat.models.user.UserName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<Token, UserName> {
    Optional<Token> findByNumber(String number);
}
