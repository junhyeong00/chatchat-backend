package com.junhyeong.chatchat.models.token;

import com.junhyeong.chatchat.models.user.UserName;
import com.junhyeong.chatchat.utils.JwtUtil;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Token {
    @Id
    @Embedded
    private UserName userName;

    private String number;

    public Token() {
    }

    public Token(UserName userName, String number) {
        this.userName = userName;
        this.number = number;
    }

    public static Token of(UserName userName, String token) {
        return new Token(userName, token);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Token otherToken = (Token) object;

        return Objects.equals(userName, otherToken.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

    public UserName userName() {
        return userName;
    }

    public String getNextAccessToken(JwtUtil jwtUtil) {
        return jwtUtil.encode(userName);
    }

    public String getNextVersion(JwtUtil jwtUtil) {
        String tokenNumber = jwtUtil.encode(UUID.randomUUID());

        this.number = tokenNumber;

        return number;
    }
}
