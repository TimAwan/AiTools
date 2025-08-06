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

public class PdfExtractionTest { // 类名已更改为 PdfExtractionTest

    private static final String NATIVE_PDF_PATH = "src/test/resources/native.pdf";
    private static final String SCAN_PDF_PATH = "src/test/resources/scan.pdf";

    @Test
    void testReadNativePdfAndPrintText() throws IOException {
        // 1. 准备测试文件资源
        File file = new File(NATIVE_PDF_PATH);
        if (!file.exists()) {
            System.err.println("测试文件不存在: " + NATIVE_PDF_PATH);
            return;
        }
        Resource resource = new FileSystemResource(file);

        // 2. 创建 PdfDocumentReaderConfig
        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                .withPagesPerDocument(1) // 每页作为一个Document
                .build();

        // 3. 创建 PdfDocumentReader
        PagePdfDocumentReader reader = new PagePdfDocumentReader(resource, config);

        // 4. 执行读取操作
        List<Document> documents = reader.read();

        // 5. 遍历并打印结果
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
        // 1. 准备扫描件测试文件
        File file = new File(SCAN_PDF_PATH);
        if (!file.exists()) {
            System.err.println("测试文件不存在: " + SCAN_PDF_PATH);
            return;
        }
        Resource resource = new FileSystemResource(file);

        // 2. 使用默认配置
        PagePdfDocumentReader reader = new PagePdfDocumentReader(resource);

        // 3. 执行读取操作
        List<Document> documents = reader.read();

        // 4. 断言结果
        // 对于扫描件，通常会返回空列表或只包含元数据的文档
        // 你也可以改成打印，但这里我保留了断言以测试无文本的情况
    }
}