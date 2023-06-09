package com.junhyeong.chatchat.models.company;

import com.junhyeong.chatchat.models.commom.Image;
import com.junhyeong.chatchat.models.commom.Name;
import com.junhyeong.chatchat.models.commom.Username;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CompanyTest {
    @Test
    void edit() {
        Username userName = new Username("company123");
        Company company = Company.fake(userName, new Name("악덕기업"));

        Name editedName = new Name("착한기업");

        company.edit(
                editedName,
                new Description("착한기업"),
                new Image("이미지"),
                ProfileVisibility.HIDDEN
        );

        assertThat(company.name()).isEqualTo(editedName);
    }
}
