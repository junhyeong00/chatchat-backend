package com.junhyeong.chatchat.controllers.company;

import com.junhyeong.chatchat.applications.autoReply.CreateAutoReplyService;
import com.junhyeong.chatchat.applications.autoReply.EditAutoReplyService;
import com.junhyeong.chatchat.applications.autoReply.GetAutoRepliesService;
import com.junhyeong.chatchat.dtos.CreateAutoReplyRequest;
import com.junhyeong.chatchat.dtos.EditAutoReplyRequest;
import com.junhyeong.chatchat.exceptions.AutoReplyNotFound;
import com.junhyeong.chatchat.exceptions.NotHaveEditAutoReplyAuthority;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.autoReply.Question;
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

@WebMvcTest(AutoReplyController.class)
class AutoReplyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetAutoRepliesService getAutoRepliesService;

    @MockBean
    private CreateAutoReplyService createAutoReplyService;

    @MockBean
    private EditAutoReplyService editAutoReplyService;

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

    @Test
    void create() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        CreateAutoReplyRequest createAutoReplyRequest = CreateAutoReplyRequest.fake(new Question("질문"));

        given(createAutoReplyService.create(username, createAutoReplyRequest))
                .willReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/auto-replies")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"question\": \"질문\", " +
                                "\"answer\": \"답변\"" +
                                "}"))
                .andExpect(status().isCreated());
    }

    @Test
    void createWithUnauthorized() throws Exception {
        Username invalidUsername = new Username("xxx");
        String token = jwtUtil.encode(invalidUsername);

        given(createAutoReplyService.create(any(), any()))
                .willThrow(Unauthorized.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/auto-replies")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"question\": \"질문\", " +
                                "\"answer\": \"답변\"" +
                                "}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createWithBlankQuestion() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.post("/auto-replies")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"question\": \"\", " +
                                "\"answer\": \"답변\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithBlankAnswer() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        mockMvc.perform(MockMvcRequestBuilders.post("/auto-replies")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"question\": \"질문\", " +
                                "\"answer\": \"\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void edit() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        Long autoReplyId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/auto-replies/%d", autoReplyId))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"question\": \"질문\", " +
                                "\"answer\": \"답변\"" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    void editWithUnauthorized() throws Exception {
        Username invalidUsername = new Username("xxx");
        String token = jwtUtil.encode(invalidUsername);

        Long autoReplyId = 1L;

        doAnswer(invocation -> {
            throw new Unauthorized();
        }).when(editAutoReplyService).edit(any(),any());

        mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/auto-replies/%d", autoReplyId))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"question\": \"질문\", " +
                                "\"answer\": \"답변\"" +
                                "}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void editWithNotHaveEditAutoReplyAuthority() throws Exception {
        Username invalidUsername = new Username("xxx");
        String token = jwtUtil.encode(invalidUsername);

        Long autoReplyId = 1L;

        doAnswer(invocation -> {
            throw new NotHaveEditAutoReplyAuthority();
        }).when(editAutoReplyService).edit(any(),any());

        mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/auto-replies/%d", autoReplyId))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"question\": \"질문\", " +
                                "\"answer\": \"답변\"" +
                                "}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void editWithAutoReplyNotFound() throws Exception {
        Username invalidUsername = new Username("xxx");
        String token = jwtUtil.encode(invalidUsername);

        Long autoReplyId = 999L;

        doAnswer(invocation -> {
            throw new AutoReplyNotFound();
        }).when(editAutoReplyService).edit(any(),any());

        mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/auto-replies/%d", autoReplyId))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"question\": \"질문\", " +
                                "\"answer\": \"답변\"" +
                                "}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void editWithBlankQuestion() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        Long autoReplyId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/auto-replies/%d", autoReplyId))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"question\": \"\", " +
                                "\"answer\": \"답변\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editWithBlankAnswer() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        Long autoReplyId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/auto-replies/%d", autoReplyId))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"question\": \"질문\", " +
                                "\"answer\": \"\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }
}
