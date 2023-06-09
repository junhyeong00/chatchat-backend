package com.junhyeong.chatchat.controllers;

import com.junhyeong.chatchat.applications.company.EditCompanyService;
import com.junhyeong.chatchat.applications.company.GetCompanyProfileService;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetCompanyProfileService getCompanyProfileService;

    @MockBean
    private EditCompanyService editCompanyService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void companyProfile() throws Exception {
        Username userName = new Username("company123");
        String token = jwtUtil.encode(userName);

        given(getCompanyProfileService.find(userName))
                .willReturn(Company.fake(userName));

        mockMvc.perform(MockMvcRequestBuilders.get("/companies/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void companyProfileWithCompanyNotFound() throws Exception {
        Username invalidUserName = new Username("xxx");
        String token = jwtUtil.encode(invalidUserName);

        given(getCompanyProfileService.find(invalidUserName))
                .willThrow(CompanyNotFound.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/companies/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void editCompany() throws Exception {
        Username userName = new Username("company123");
        String token = jwtUtil.encode(userName);

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\":\"악덕기업\", " +
                                "   \"description\":\"악덕기업입니다\", " +
                                "   \"imageUrl\":\"이미지\"" +
                                "}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void editCompanyWithBlankName() throws Exception {
        Username userName = new Username("company123");
        String token = jwtUtil.encode(userName);

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\":\"\", " +
                                "   \"description\":\"악덕기업입니다\", " +
                                "   \"imageUrl\":\"이미지\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editCompanyWithInvalidName() throws Exception {
        Username userName = new Username("company123");
        String token = jwtUtil.encode(userName);

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\":\"123456789012345678901234567890\", " +
                                "   \"description\":\"악덕기업입니다\", " +
                                "   \"imageUrl\":\"이미지\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editCompanyWithCompanyNotFound() throws Exception {
        Username invalidUserName = new Username("xxx");
        String token = jwtUtil.encode(invalidUserName);

        doAnswer(invocation -> {
            throw new CompanyNotFound();
        }).when(editCompanyService).edit(any(),any());

        mockMvc.perform(MockMvcRequestBuilders.patch("/companies/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"name\":\"악덕기업\", " +
                                "   \"description\":\"악덕기업입니다\", " +
                                "   \"imageUrl\":\"이미지\"" +
                                "}"))
                .andExpect(status().isNotFound());
    }
}
