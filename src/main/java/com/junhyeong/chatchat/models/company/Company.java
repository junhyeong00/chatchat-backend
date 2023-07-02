package com.junhyeong.chatchat.models.company;

import com.junhyeong.chatchat.dtos.ChatRoomDetailDto;
import com.junhyeong.chatchat.dtos.CompanyDetailDto;
import com.junhyeong.chatchat.dtos.CompanyProfileDto;
import com.junhyeong.chatchat.dtos.MessageDto;
import com.junhyeong.chatchat.dtos.PageDto;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Company {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Username username;

    @Embedded
    private Password password;

    @Embedded
    private Name name;

    @Embedded
    private Description description;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "profile_image"))
    private Image profileImage;

    @Enumerated(EnumType.STRING)
    private ProfileVisibility profileVisibility;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Company() {
    }

    public Company(Long id, Username username, Name name,
                   Description description, Image profileImage) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.description = description;
        this.profileImage = profileImage;
        this.profileVisibility = ProfileVisibility.HIDDEN;
    }

    public Company(Username username, Name name,
                   Description description, Image profileImage) {
        this.username = username;
        this.name = name;
        this.description = description;
        this.profileImage = profileImage;
        this.profileVisibility = ProfileVisibility.HIDDEN;
    }

    public Company(Username username, Name name) {
        this.username = username;
        this.name = name;
        this.description = new Description("");
        this.profileImage = new Image(Image.DEFAULT_PROFILE_IMAGE);
        this.profileVisibility = ProfileVisibility.HIDDEN;
    }

    public static Company fake(Username username) {
        return new Company(
                1L,
                username,
                new Name("악덕기업"),
                new Description("악덕기업입니다"),
                new Image(Image.DEFAULT_PROFILE_IMAGE));
    }

    public static Company fake(Username username, Name name) {
        return new Company(
                1L,
                username,
                name,
                new Description("악덕기업입니다"),
                new Image(Image.DEFAULT_PROFILE_IMAGE));
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
        return new CompanyProfileDto(id, name.value(),
                description.value(), profileImage.value(),
                profileVisibility.equals(ProfileVisibility.VISIBLE));
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

    public Description description() {
        return description;
    }

    public Image profileImage() {
        return profileImage;
    }

    public LocalDateTime registeredAt() {
        return registeredAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public void edit(Name name, Description description, Image profileImage, ProfileVisibility profileVisibility) {
        this.name = name;
        this.description = description;
        this.profileImage = profileImage;
        this.profileVisibility = profileVisibility;
    }

    public CompanyDetailDto toDetailDto() {
        return new CompanyDetailDto(
                id,
                name.value(),
                description.value(),
                profileImage.value());
    }

    public ChatRoomDetailDto toRoomDetailDto(Long chatRoomId, List<MessageDto> messages, PageDto page) {
        return new ChatRoomDetailDto(
                chatRoomId,
                this.id,
                this.name.value(),
                this.profileImage.value(),
                messages,
                page);
    }
}
