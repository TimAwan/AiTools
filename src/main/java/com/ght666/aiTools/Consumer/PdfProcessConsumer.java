package com.ght666.aiTools.Consumer;

import com.ght666.aiTools.config.RabbitMQConfig;
import com.ght666.aiTools.entity.po.PdfUploadMessage;
import com.ght666.aiTools.entity.vo.PdfProcessResult;
import com.ght666.aiTools.service.IPdfProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * @author: 1012ght
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PdfProcessConsumer {
    private final IPdfProcessService pdfProcessService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.PDF_PROCESS_QUEUE)
    public void processPdf(PdfUploadMessage message) {
        log.info("PDF处理进入消费者,开始处理PDF chatId={},fileName={}", message.getChatId(), message.getFileName());
        long startTime = System.currentTimeMillis();
        try {

            if (!Files.exists(Paths.get(message.getFilePath()))) {
                throw new RuntimeException("文件不存在: " + message.getFilePath());
            }
            PdfProcessResult result = pdfProcessService.processPdf(message);
            sendProcessResult(result);
            long processTime = System.currentTimeMillis() - startTime;
            log.info("PDF处理完成: chatId={}, 耗时={}ms", message.getChatId(), processTime);
        } catch (RuntimeException e) {
            log.error("PDF处理失败: chatId={}, fileName={}", message.getChatId(), message.getFileName(), e);
            // 失败结果
            PdfProcessResult errorResult = PdfProcessResult.builder()
                    .chatId(message.getChatId())
                    .status("FAILED")
                    .message(e.getMessage())
                    .progressTime(System.currentTimeMillis() - startTime)
                    .build();
            sendProcessResult(errorResult);
        }

    }

    private void sendProcessResult(PdfProcessResult result) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PDF_RESULT_EXCHANGE,
                "pdf.result",
                result
        );
    }
}
