package com.junhyeong.chatchat.exceptions;

public class FileUploadFail extends RuntimeException {
    public FileUploadFail(String message) {
        super(message);
    }
}
