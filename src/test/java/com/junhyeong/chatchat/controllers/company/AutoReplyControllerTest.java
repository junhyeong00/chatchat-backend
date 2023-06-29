package com.junhyeong.chatchat.controllers.company;

import com.junhyeong.chatchat.applications.autoReply.GetAutoRepliesService;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutoReplyController.class)
class AutoReplyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetAutoRepliesService getAutoRepliesService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void autoReplies() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        List<AutoReply> autoReplies = List.of(AutoReply.fake(username));

        given(getAutoRepliesService.autoReplies(username))
                .willReturn(autoReplies);

        mockMvc.perform(MockMvcRequestBuilders.get("/auto-replies")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void autoRepliesWithUnauthorized() throws Exception {
        Username invalidUsername = new Username("xxx");

        String token = jwtUtil.encode(invalidUsername);

        given(getAutoRepliesService.autoReplies(invalidUsername))
                .willThrow(Unauthorized.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/auto-replies")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }
}
