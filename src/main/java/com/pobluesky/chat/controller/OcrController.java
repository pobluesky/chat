package com.pobluesky.chat.controller;

import com.pobluesky.chat.entity.ProductType;
import com.pobluesky.chat.service.IntegratedOcrGptService;
import com.pobluesky.global.util.ResponseFactory;
import com.pobluesky.global.util.model.JsonResult;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ocr")
public class OcrController {

    private final IntegratedOcrGptService integratedOcrGptService;

    @PostMapping(value ="/{userId}")
    @Operation(summary = "사용자가 업로드한 파일을 OCR 처리하여 텍스트 추출")
    public ResponseEntity<JsonResult<?>> processOcrFile(
        @RequestHeader("Authorization") String token,
        @PathVariable Long userId,
        @RequestParam("file") MultipartFile file,
        @RequestParam("productType") ProductType productType
    ) {

        JsonResult<?> response = integratedOcrGptService.processFileAndStructureData(token, userId, file, productType);

        return ResponseEntity.ok(ResponseFactory.getSuccessJsonResult(response));
    }

}
