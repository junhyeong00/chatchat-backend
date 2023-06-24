package com.junhyeong.chatchat.controllers.company;

import com.junhyeong.chatchat.applications.login.CompanyLoginService;
import com.junhyeong.chatchat.controllers.company.CompanySessionController;
import com.junhyeong.chatchat.dtos.TokenDto;
import com.junhyeong.chatchat.exceptions.LoginFailed;
import com.junhyeong.chatchat.models.commom.Password;
import com.junhyeong.chatchat.models.commom.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanySessionController.class)
@ActiveProfiles("test")
class CompanySessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyLoginService loginService;

    @BeforeEach
    void setup() {
        Username userName = new Username("test123");
        Password password = new Password("Password1234!");

        Username wrongUserName = new Username("wrong123");
        Password wrongPassword = new Password("notPassword1234!");

        given(loginService.login(userName, password))
                .willReturn(TokenDto.fake());

        given(loginService.login(wrongUserName, wrongPassword))
                .willThrow(LoginFailed.class);

        given(loginService.login(wrongUserName, password))
                .willThrow(LoginFailed.class);

        given(loginService.login(userName, wrongPassword))
                .willThrow(LoginFailed.class);
    }

    @Test
    void loginSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/company/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"username\": \"test123\", " +
                                "\"password\": \"Password1234!\"" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(
                        containsString("\"accessToken\"")
                ));
    }

    @Test
    void loginFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/company/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"username\": \"wrong123\", " +
                                "\"password\": \"notPassword1234!\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPasswordIsInvalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/company/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"username\":\"test123\", " +
                                "\"password\": \"notPassword1234!\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUsernameIsInvalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/company/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"username\":\"wrong123\", " +
                                "\"password\": \"Password1234!\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUserNameIsBlank() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/company/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"username\":\"\", " +
                                "\"password\": \"Password1234!\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPasswordIsBlank() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/company/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"username\":\"test123\", " +
                                "\"password\": \"\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }
}
