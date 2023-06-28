package com.junhyeong.chatchat.controllers.common;

import com.junhyeong.chatchat.applications.file.FileUploadService;
import com.junhyeong.chatchat.dtos.FileUploadResultDto;
import com.junhyeong.chatchat.exceptions.FileUploadFail;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("files")
public class FileController {
    private final FileUploadService fileUploadService;

    public FileController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FileUploadResultDto upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam String folder
    ) {
        try {
            String url = fileUploadService.upload(file, folder);

            return new FileUploadResultDto(url);
        } catch (Exception exception) {
            throw new FileUploadFail(exception.getMessage());
        }
    }

    @ExceptionHandler(FileUploadFail.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String fileUploadFailed(Exception exception) {
        return exception.getMessage();
    }
}
