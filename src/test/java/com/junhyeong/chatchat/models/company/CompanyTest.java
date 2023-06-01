package com.junhyeong.chatchat.models.company;

import com.junhyeong.chatchat.models.commom.Image;
import com.junhyeong.chatchat.models.commom.Name;
import com.junhyeong.chatchat.models.commom.UserName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CompanyTest {
    @Test
    void edit() {
        UserName userName = new UserName("company123");
        Company company = Company.fake(userName, new Name("악덕기업"));

        Name editedName = new Name("착한기업");

        company.edit(
                editedName,
                new Description("착한기업"),
                new Image("이미지")
        );

        assertThat(company.name()).isEqualTo(editedName);
    }
}
