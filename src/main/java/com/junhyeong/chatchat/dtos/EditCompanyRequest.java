package com.junhyeong.chatchat.dtos;

import com.junhyeong.chatchat.models.commom.Image;
import com.junhyeong.chatchat.models.commom.Name;
import com.junhyeong.chatchat.models.company.Description;
import com.junhyeong.chatchat.models.company.ProfileVisibility;

public class EditCompanyRequest {
    private Name name;
    private Description description;
    private Image profileImage;
    private ProfileVisibility profileVisibility;

    public EditCompanyRequest(Name name, Description description,
                              Image profileImage, ProfileVisibility profileVisibility) {
        this.name = name;
        this.description = description;
        this.profileImage = profileImage;
        this.profileVisibility = profileVisibility;
    }

    public static EditCompanyRequest of(EditCompanyRequestDto editCompanyRequestDto) {
        return new EditCompanyRequest(
                new Name(editCompanyRequestDto.name()),
                new Description(editCompanyRequestDto.description()),
                new Image(editCompanyRequestDto.imageUrl().isBlank() ?
                        Image.DEFAULT_PROFILE_IMAGE : editCompanyRequestDto.imageUrl()),
                editCompanyRequestDto.profileVisibility() ?
                        ProfileVisibility.VISIBLE : ProfileVisibility.HIDDEN);
    }

    public static EditCompanyRequest fake(Name name) {
        return new EditCompanyRequest(
                name,
                new Description("악덕기업입니다"),
                new Image("이미지"),
                ProfileVisibility.VISIBLE);
    }

    public Name getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public ProfileVisibility getProfileVisibility() {
        return profileVisibility;
    }
}
