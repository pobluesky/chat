package com.pobluesky.chat.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "file")
public interface FileClient {

    @PostMapping(value = "/api/upload", consumes = "multipart/form-data")
    FileInfo uploadFile(@RequestParam("file") MultipartFile file);
}
