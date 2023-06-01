package com.junhyeong.chatchat.models.company;

import com.junhyeong.chatchat.dtos.CompanyProfileDto;
import com.junhyeong.chatchat.exceptions.LoginFailed;
import com.junhyeong.chatchat.models.commom.Image;
import com.junhyeong.chatchat.models.commom.Name;
import com.junhyeong.chatchat.models.commom.Password;
import com.junhyeong.chatchat.models.commom.UserName;
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

@Entity
public class Company {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private UserName userName;

    @Embedded
    private Password password;

    @Embedded
    private Name name;

    @Embedded
    private Description description;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "profile_image"))
    private Image profileImage;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Company() {
    }

    public Company(Long id, UserName userName, Name name,
                   Description description, Image profileImage) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.description = description;
        this.profileImage = profileImage;
    }

    public Company(UserName userName, Name name,
                   Description description, Image profileImage) {
        this.userName = userName;
        this.name = name;
        this.description = description;
        this.profileImage = profileImage;
    }

    public static Company fake(UserName userName) {
        return new Company(
                1L,
                userName,
                new Name("악덕기업"),
                new Description("악덕기업입니다"),
                new Image("이미지"));
    }

    public static Company fake(UserName userName, Name name) {
        return new Company(
                1L,
                userName,
                name,
                new Description("악덕기업입니다"),
                new Image("이미지"));
    }

    public void changePassword(Password password, PasswordEncoder passwordEncoder) {
        this.password = Password.of(passwordEncoder.encode(password.getValue()));
    }

    public void authenticate(Password password, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(password.getValue(), this.password.getValue())) {
            throw new LoginFailed();
        }
    }

    public CompanyProfileDto toProfileDto() {
        return new CompanyProfileDto(id, name.value(), description.value(), profileImage.value());
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

    public Name name() {
        return name;
    }

    public Description description() {
        return description;
    }

    public Image pProfileImage() {
        return profileImage;
    }

    public LocalDateTime registeredAt() {
        return registeredAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public void edit(Name name, Description description, Image profileImage) {
        this.name = name;
        this.description = description;
        this.profileImage = profileImage;
    }
}
