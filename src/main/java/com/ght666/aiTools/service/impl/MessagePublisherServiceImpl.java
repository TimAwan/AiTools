package com.ght666.aiTools.service.impl;

import com.ght666.aiTools.entity.po.PdfUploadMessage;
import com.ght666.aiTools.entity.vo.PdfProcessResult;
import com.ght666.aiTools.service.IMessagePublisherService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: 1012ght
 */
@Service
public class MessagePublisherServiceImpl implements IMessagePublisherService {
    @Autowired
    private  RabbitTemplate rabbitTemplate;

    @Override
    public void publishPdfUploadMessage(PdfUploadMessage message) {
    }
    @Override
    public void publishProcessResult(PdfProcessResult result) {

    }
}
