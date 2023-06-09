package com.junhyeong.chatchat.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.junhyeong.chatchat.models.commom.Username;

import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    private final Algorithm algorithm;
    private final Long ACCESS_TOKEN_VALIDATION_SECOND = 1000L * 60 * 30;
    private final Long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 60 * 24 * 14;

    public JwtUtil(String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String encode(Username userName) {
        return JWT.create()
                .withClaim("userName", userName.value())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDATION_SECOND))
                .sign(algorithm);
    }

    public String encode(UUID uuid) {
        return JWT.create()
                .withClaim("uuid", uuid.toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDATION_SECOND))
                .sign(algorithm);
    }

    public Username decode(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT verify = verifier.verify(token);

        String value = verify.getClaim("userName").asString();

        return new Username(value);
    }

    public String decodeRefreshToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT verify = verifier.verify(token);

        return verify.getClaim("uuid").asString();
    }
}
