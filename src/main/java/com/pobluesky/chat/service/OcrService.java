package com.pobluesky.chat.service;

import ch.qos.logback.classic.Logger;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageSource;

import com.pobluesky.global.error.CommonException;
import com.pobluesky.global.error.ErrorCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class OcrService {

    @Value("${cloud.gcp.storage.bucket.filePath}")
    private String bucketFilePath;

    private final FileConversionService fileConversionService;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OcrService.class);

    public List<String> processFileAndDetectText(MultipartFile file) {
        try {
            List<String> gcsPaths = fileConversionService.convertFileToImages(file);

            logger.debug("File conversion completed. GCS Paths: {}", gcsPaths);

            List<String> textResults = new ArrayList<>();

            for (String gcsPath : gcsPaths) {
                String detectedText = detectTextGcs(gcsPath);
                textResults.add(detectedText);
            }
            logger.debug("aaaaaaaaaaaaa");
            return textResults;
        } catch (Exception e) {
            throw new CommonException(ErrorCode.EXTERNAL_SERVER_ERROR);
        }
    }

    public String detectTextGcs(String gcsPath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(gcsPath).build();
        Image img = Image.newBuilder().setSource(imgSource).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            StringBuilder detectedText = new StringBuilder();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    throw new CommonException(ErrorCode.OCR_PROCESS_FAIL);
                }

                if (!res.getTextAnnotationsList().isEmpty()) {
                    detectedText.append(res.getTextAnnotations(0).getDescription());
                }
            }
            return detectedText.toString();
        }
    }
}
