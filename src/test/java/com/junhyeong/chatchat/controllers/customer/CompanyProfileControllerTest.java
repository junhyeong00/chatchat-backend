package com.junhyeong.chatchat.controllers.customer;

import com.junhyeong.chatchat.applications.company.GetCompanyDetailService;
import com.junhyeong.chatchat.applications.company.GetCompanyProfilesService;
import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.dtos.CompanySummaryDto;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyProfileController.class)
class CompanyProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetCompanyProfilesService getCompanyProfilesService;

    @MockBean
    private GetCompanyDetailService getCompanyDetailService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void chatRooms() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        String keyword = "";

        Page<CompanySummaryDto> page = new PageImpl<>(List.of(CompanySummaryDto.fake()));

        given(getCompanyProfilesService.companyProfiles(username, keyword, 1))
                .willReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/companies")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void companyDetail() throws Exception {
        Username userName = new Username("customer123");
        String token = jwtUtil.encode(userName);

        Long companyId = 1L;

        given(getCompanyDetailService.find(userName, companyId))
                .willReturn(Company.fake(userName));

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/companies/%d", companyId))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void companyDetailWithUnauthorized() throws Exception {
        Username invalidUserName = new Username("xxx");
        String token = jwtUtil.encode(invalidUserName);

        Long companyId = 1L;

        given(getCompanyDetailService.find(invalidUserName, companyId))
                .willThrow(Unauthorized.class);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/companies/%d", companyId))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void companyDetailWithCompanyNotFound() throws Exception {
        Username userName = new Username("customer123");
        String token = jwtUtil.encode(userName);

        Long invalidCompanyId = 999L;

        given(getCompanyDetailService.find(userName, invalidCompanyId))
                .willThrow(CompanyNotFound.class);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/companies/%d", invalidCompanyId))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}
