package com.junhyeong.chatchat.dtos;

public class FileUploadResultDto {
    private String url;

    public FileUploadResultDto() {
    }

    public FileUploadResultDto(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
