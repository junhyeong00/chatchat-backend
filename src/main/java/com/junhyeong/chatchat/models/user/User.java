package com.junhyeong.chatchat.models.user;

import com.junhyeong.chatchat.exceptions.LoginFailed;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private UserName userName;

    private Password password;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "nickname"))
    private Nickname nickname;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(Long id, UserName userName, Nickname nickname) {
        this.id = id;
        this.userName = userName;
        this.nickname = nickname;
    }

    public User(UserName userName, Nickname nickname) {
        this.userName = userName;
        this.nickname = nickname;
    }

    public static User fake(UserName userName) {
        return new User(userName,
                new Nickname("테스터"));
    }

    public void changePassword(Password password, PasswordEncoder passwordEncoder) {
        this.password = Password.of(passwordEncoder.encode(password.getValue()));
    }

    public void authenticate(Password password, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(password.getValue(), this.password.getValue())) {
            throw new LoginFailed();
        }
    }

    public Long id() {
        return id;
    }

    public UserName userName() {
        return userName;
    }

    public Password password() {
        return password;
    }

    public Nickname nickname() {
        return nickname;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
