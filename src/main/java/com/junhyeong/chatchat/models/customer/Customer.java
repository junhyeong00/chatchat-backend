package com.junhyeong.chatchat.models.customer;

import com.junhyeong.chatchat.dtos.ChatRoomDetailDto;
import com.junhyeong.chatchat.dtos.CompanyProfileDto;
import com.junhyeong.chatchat.dtos.CustomerProfileDto;
import com.junhyeong.chatchat.dtos.MessageDto;
import com.junhyeong.chatchat.exceptions.LoginFailed;
import com.junhyeong.chatchat.models.commom.Image;
import com.junhyeong.chatchat.models.commom.Name;
import com.junhyeong.chatchat.models.commom.Password;
import com.junhyeong.chatchat.models.commom.Username;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    private Username username;

    private Password password;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name"))
    private Name name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "profile_image"))
    private Image profileImage;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Customer() {
    }

    public Customer(Long id, Username username, Name name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    public Customer(Username username, Name name) {
        this.username = username;
        this.name = name;
        this.profileImage = new Image("기본이미지");
    }

    public static Customer fake(Username username) {
        return new Customer(username,
                new Name("테스터"));
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

    public Username username() {
        return username;
    }

    public Password password() {
        return password;
    }

    public Name name() {
        return name;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ChatRoomDetailDto toRoomDetailDto(Long chatRoomId, List<MessageDto> messages) {
        return new ChatRoomDetailDto(
                chatRoomId,
                this.id,
                this.name.value(),
                this.profileImage.value(),
                messages
        );
    }

    public CustomerProfileDto toProfileDto() {
        return new CustomerProfileDto(id, name.value(), profileImage.value());
    }

    public void edit(Name name, Image profileImage) {
        this.name = name;
        this.profileImage = profileImage;
    }
}
