package com.junhyeong.chatchat.dtos;

import com.junhyeong.chatchat.models.commom.Image;
import com.junhyeong.chatchat.models.commom.Name;

public class EditCustomerRequest {
    private Name name;
    private Image profileImage;

    public EditCustomerRequest(Name name, Image profileImage) {
        this.name = name;
        this.profileImage = profileImage;
    }

    public static EditCustomerRequest of(EditCustomerRequestDto editCustomerRequestDto) {
        return new EditCustomerRequest(
                new Name(editCustomerRequestDto.name()),
                new Image(editCustomerRequestDto.imageUrl().isBlank() ?
                        Image.DEFAULT_PROFILE_IMAGE : editCustomerRequestDto.imageUrl()));
    }

    public static EditCustomerRequest fake(Name name) {
        return new EditCustomerRequest(
                name,
                new Image("이미지"));
    }

    public Name getName() {
        return name;
    }

    public Image getProfileImage() {
        return profileImage;
    }
}
