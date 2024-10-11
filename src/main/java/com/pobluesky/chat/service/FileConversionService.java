package com.pobluesky.chat.service;

import com.pobluesky.global.error.CommonException;
import com.pobluesky.global.error.ErrorCode;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileConversionService {

    private static final Logger logger = LoggerFactory.getLogger(FileConversionService.class);

    private final PdfConversionService pdfConversionService;

    private final ExcelConversionService excelConversionService;

    public List<String> convertFileToImages(MultipartFile file) {
        try {
            String uniqueId = generateUniqueId();
            String fileName = file.getOriginalFilename();
            String fileExtension = getFileExtension(fileName);

            logger.debug("uniqueId: {}", uniqueId);
            logger.debug("fileName: {}", fileName);
            logger.debug("fileExtension: {}", fileExtension);

            switch (fileExtension.toLowerCase()) {
                case "pdf":
                    return pdfConversionService.convertPdfToImages(file.getInputStream(), uniqueId);
                case "xlsx":
                case "xls":
                    return Collections.singletonList(excelConversionService.convertExcelToImage(file.getInputStream(), uniqueId));
                default:
                    throw new CommonException(ErrorCode.UNSUPPORTED_FILE_TYPE);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.FILE_CONVERSION_ERROR);
        }
    }

    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            throw new CommonException(ErrorCode.INVALID_FILE_NAME);
        }
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            throw new CommonException(ErrorCode.INVALID_FILE_NAME);
        }
        return fileName.substring(lastIndexOf + 1);
    }
}
