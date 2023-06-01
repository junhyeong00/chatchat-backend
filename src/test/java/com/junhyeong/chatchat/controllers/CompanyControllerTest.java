package com.junhyeong.chatchat.controllers;

import com.junhyeong.chatchat.applications.company.GetCompanyProfileService;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.models.commom.UserName;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetCompanyProfileService getCompanyProfileService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void companyProfile() throws Exception {
        UserName userName = new UserName("company123");
        String token = jwtUtil.encode(userName);

        given(getCompanyProfileService.find(userName))
                .willReturn(Company.fake(userName));

        mockMvc.perform(MockMvcRequestBuilders.get("/companies/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void companyProfileWithCompanyNotFound() throws Exception {
        UserName invalidUserName = new UserName("xxx");
        String token = jwtUtil.encode(invalidUserName);

        given(getCompanyProfileService.find(invalidUserName))
                .willThrow(CompanyNotFound.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/companies/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}
