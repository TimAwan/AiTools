package com.ght666.aiTools;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PdfReaderTest {

    private static final String NATIVE_PDF_PATH = "src/test/resources/native.pdf";
    private static final String SCAN_PDF_PATH = "src/test/resources/scan.pdf";

    @Test
    void testReadNativePdf() throws IOException {
        File file = new File(NATIVE_PDF_PATH);
        if (!file.exists()) {
            System.err.println("测试文件不存在: " + NATIVE_PDF_PATH);
            return;
        }
        Resource resource = new FileSystemResource(file);

        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults()) // 使用默认格式化器
                .withPagesPerDocument(1) // 每页作为一个Document
                .build();
        PagePdfDocumentReader reader = new PagePdfDocumentReader(resource, config);
        List<Document> documents = reader.read();
        assertNotNull(documents);
        assertFalse(documents.isEmpty());
        Document firstDocument = documents.get(0);
        assertNotNull(firstDocument.getText());
        assertFalse(firstDocument.getText().isEmpty());
        assertTrue(firstDocument.getText().contains("团队协作能力"));
        assertTrue(firstDocument.getMetadata().containsKey("page_number"));
        assertTrue(firstDocument.getMetadata().get("page_number").equals(1));
    }
    @Test
    void testReadScanPdf() throws IOException {
        File file = new File(SCAN_PDF_PATH);
        if (!file.exists()) {
            System.err.println("测试文件不存在: " + SCAN_PDF_PATH);
            return;
        }
        Resource resource = new FileSystemResource(file);
        PagePdfDocumentReader reader = new PagePdfDocumentReader(resource);
        List<Document> documents = reader.read();
        assertNotNull(documents);
        assertTrue(documents.stream().allMatch(doc -> doc.getText().isEmpty()));
    }
}