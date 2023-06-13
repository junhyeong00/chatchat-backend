package com.junhyeong.chatchat.controllers.common;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.junhyeong.chatchat.applications.token.IssueTokenService;
import com.junhyeong.chatchat.controllers.common.TokenController;
import com.junhyeong.chatchat.dtos.TokenDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(TokenController.class)
class TokenControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IssueTokenService issueTokenService;

    @Test
    void whenRefreshTokenIsNotExpired() throws Exception {
        String token = "not.expired.token";
        Cookie cookie = new Cookie("refreshToken", token);

        given(issueTokenService.reissue(token))
                .willReturn(TokenDto.fake());

        mockMvc.perform(MockMvcRequestBuilders.post("/token")
                        .cookie(cookie))
                .andExpect(status().isCreated())
                .andExpect(content().string(
                        containsString("\"accessToken\"")
                ));
    }

    @Test
    void whenRefreshTokenExpired() throws Exception {
        String token = "is.expired.token";
        Cookie cookie = new Cookie("refreshToken", token);

        given(issueTokenService.reissue(token))
                .willThrow(TokenExpiredException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/token")
                        .cookie(cookie))
                .andExpect(status().isBadRequest());
    }
}