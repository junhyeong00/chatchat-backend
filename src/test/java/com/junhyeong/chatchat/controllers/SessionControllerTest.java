package com.junhyeong.chatchat.controllers;

import com.junhyeong.chatchat.applications.LoginService;
import com.junhyeong.chatchat.dtos.TokenDto;
import com.junhyeong.chatchat.exceptions.LoginFailed;
import com.junhyeong.chatchat.models.user.Password;
import com.junhyeong.chatchat.models.user.UserName;
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

@WebMvcTest(SessionController.class)
@ActiveProfiles("test")
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @BeforeEach
    void setup() {
        UserName userName = new UserName("test123");
        Password password = new Password("Password1234!");

        UserName wrongUserName = new UserName("wrong123");
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
        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"userName\": \"test123\", " +
                                "\"password\": \"Password1234!\"" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(
                        containsString("\"accessToken\"")
                ));
    }

    @Test
    void loginFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"userName\": \"wrong123\", " +
                                "\"password\": \"notPassword1234!\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPasswordIsInvalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"userName\":\"test123\", " +
                                "\"password\": \"notPassword1234!\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUsernameIsInvalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"userName\":\"wrong123\", " +
                                "\"password\": \"Password1234!\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUserNameIsBlank() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"userName\":\"\", " +
                                "\"password\": \"Password1234!\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPasswordIsBlank() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"userName\":\"test123\", " +
                                "\"password\": \"\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }
}
