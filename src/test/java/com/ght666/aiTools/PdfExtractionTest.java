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

public class PdfExtractionTest {

    private static final String NATIVE_PDF_PATH = "src/test/resources/native.pdf";
    private static final String SCAN_PDF_PATH = "src/test/resources/scan.pdf";

    @Test
    void testReadNativePdfAndPrintText() throws IOException {
        File file = new File(NATIVE_PDF_PATH);
        if (!file.exists()) {
            System.err.println("测试文件不存在: " + NATIVE_PDF_PATH);
            return;
        }
        Resource resource = new FileSystemResource(file);
        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                .withPagesPerDocument(1)
                .build();
        PagePdfDocumentReader reader = new PagePdfDocumentReader(resource, config);
        List<Document> documents = reader.read();
        if (documents != null && !documents.isEmpty()) {
            System.out.println("--- 从 PDF 中提取的文本内容 ---");
            for (int i = 0; i < documents.size(); i++) {
                Document doc = documents.get(i);
                System.out.println("------------------------------------------");
                System.out.printf("Page: %s\n", doc.getMetadata().get("page_number"));
                System.out.println(doc.getText());
                System.out.println("------------------------------------------");
            }
            System.out.println("--- 提取结束 ---");
        } else {
            System.out.println("未从 PDF 中提取到任何文档或文本内容。");
        }
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
    }
}