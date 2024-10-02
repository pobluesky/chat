package com.pobluesky.global.error;

public class FileUploadException extends RuntimeException {
    public FileUploadException() {
        super("File Upload Error");
    }
}
