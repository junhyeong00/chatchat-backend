package com.junhyeong.chatchat.applications.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;

@ActiveProfiles("test")
class FileUploadServiceTest {
    private FileService fileService;
    private FileUploadService fileUploadService;

    @BeforeEach
    void setup() {
        fileService = mock(FileService.class);
        fileUploadService = new FileUploadService(fileService);
    }

    @Test
    void whenFileFormatIsInvalid() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "image", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        String folderName = "review-image";

        willThrow(IllegalArgumentException.class).given(fileService).upload(any(), any(), any());

        assertThrows(IllegalArgumentException.class, () -> fileUploadService.upload(file, folderName));
    }

    @Test
    void whenFileUploadSuccess() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "image.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        String folderName = "review-image";

        String fileUrl = "url";

        given(fileService.getFileUrl(any())).willReturn(fileUrl);

        String url = fileUploadService.upload(file, folderName);

        assertThat(fileUrl).isEqualTo(url);
    }
}
