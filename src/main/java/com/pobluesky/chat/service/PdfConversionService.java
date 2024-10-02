package com.pobluesky.chat.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import com.pobluesky.global.error.CommonException;
import com.pobluesky.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfConversionService {

    private final Storage storage;

    @Value("${cloud.gcp.storage.bucket.name}")
    private String bucketName;

    public PdfConversionService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public List<String> convertPdfToImages(InputStream is, String uniqueId) {
        List<String> savedImgUrls = new ArrayList<>();

        try (PDDocument pdfDoc = PDDocument.load(is)) {
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDoc);

            for (int i = 0; i < pdfDoc.getNumberOfPages(); i++) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);

                try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    ImageIO.write(bim, "png", os);
                    byte[] imageBytes = os.toByteArray();

                    String blobName = uniqueId + "/page_" + (i + 1) + ".png";
                    storage.create(
                        BlobInfo.newBuilder(bucketName, blobName).build(),
                        imageBytes
                    );

                    String gcsPath = "gs://" + bucketName + "/" + blobName;
                    savedImgUrls.add(gcsPath);
                }
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.PDF_CONVERSION_FAILED);
        }

        if (savedImgUrls.isEmpty()) {
            throw new CommonException(ErrorCode.PDF_CONVERSION_NO_IMAGES);
        }

        return savedImgUrls;
    }
}
