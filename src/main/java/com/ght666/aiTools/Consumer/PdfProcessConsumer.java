package com.ght666.aiTools.Consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;



/**
 * @author: 1012ght
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PdfProcessConsumer {
    //private final PdfProcessService pdfProcessService;
    private final RabbitTemplate rabbitTemplate;
}
