package com.junhyeong.chatchat.controllers.customer;

import com.junhyeong.chatchat.applications.company.GetCompanyProfilesService;
import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.dtos.CompanySummaryDto;
import com.junhyeong.chatchat.models.commom.Username;
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
}
