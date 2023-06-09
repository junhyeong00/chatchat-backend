package com.junhyeong.chatchat.repositories.token;

import com.junhyeong.chatchat.models.token.Token;
import com.junhyeong.chatchat.models.commom.Username;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<Token, Username> {
    Optional<Token> findByNumber(String number);
}
