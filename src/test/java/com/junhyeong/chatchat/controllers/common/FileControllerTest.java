package com.junhyeong.chatchat.controllers.common;

import com.junhyeong.chatchat.applications.file.FileUploadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(FileController.class)
class FileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileUploadService fileUploadService;

    @Test
    void whenUploadSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "image.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        String folderName = "review-image";

        given(fileUploadService.upload(file, folderName)).willReturn("url");

        mockMvc.perform(MockMvcRequestBuilders.multipart(String.format("/files?folder=%s", folderName))
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(content().string(
                        containsString("\"url\"")
                ));
    }

    @Test
    void whenUploadFail() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "image.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        String folderName = "review-image";

        given(fileUploadService.upload(file, folderName)).willThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.multipart(String.format("/files?folder=%s", folderName))
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

}
