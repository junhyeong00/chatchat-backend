package com.junhyeong.chatchat.controllers.customer;

import com.junhyeong.chatchat.applications.autoReply.GetAutoReplyQuestionsService;
import com.junhyeong.chatchat.applications.autoReply.SendAutoReplyService;
import com.junhyeong.chatchat.dtos.AutoReplyQuestionDto;
import com.junhyeong.chatchat.exceptions.ChatRoomNotFound;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerAutoReplyController.class)
class CustomerAutoReplyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetAutoReplyQuestionsService getAutoReplyQuestionsService;

    @MockBean
    private SendAutoReplyService sendAutoReplyService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void autoReplyQuestions() throws Exception {
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        List<AutoReplyQuestionDto> autoReplies = List.of(AutoReply.fake(username)).stream()
                .map(AutoReply::toQuestionDto).toList();

        given(getAutoReplyQuestionsService.questions(any(), any()))
                .willReturn(autoReplies);

        mockMvc.perform(MockMvcRequestBuilders.get("/auto-replies")
                        .header("Authorization", "Bearer " + token)
                        .param("companyId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void autoReplyQuestionsWithUnauthorized() throws Exception {
        Username invalidUsername = new Username("xxx");

        String token = jwtUtil.encode(invalidUsername);

        given(getAutoReplyQuestionsService.questions(any(), any()))
                .willThrow(Unauthorized.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/auto-replies")
                        .header("Authorization", "Bearer " + token)
                        .param("companyId", "1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void autoReplyQuestionsWithCompanyNotFound() throws Exception {
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        given(getAutoReplyQuestionsService.questions(any(), any()))
                .willThrow(CompanyNotFound.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/auto-replies")
                        .header("Authorization", "Bearer " + token)
                        .param("companyId", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void sendAutoReply() throws Exception {
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.post("/auto-replies/1")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"chatRoomId\":\"1\"" +
                                "}"))
                .andExpect(status().isCreated());
    }

    @Test
    void sendAutoReplyWithUnauthorized() throws Exception {
        Username invalidUsername = new Username("xxx");

        String token = jwtUtil.encode(invalidUsername);

        doAnswer(invocation -> {
            throw new Unauthorized();
        }).when(sendAutoReplyService).send(any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/auto-replies/1")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"chatRoomId\":\"1\"" +
                                "}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void sendAutoReplyWithChatRoomNotFound() throws Exception {
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        doAnswer(invocation -> {
            throw new ChatRoomNotFound();
        }).when(sendAutoReplyService).send(any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/auto-replies/1")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "   \"chatRoomId\":\"999\"" +
                                "}"))
                .andExpect(status().isNotFound());
    }
}
