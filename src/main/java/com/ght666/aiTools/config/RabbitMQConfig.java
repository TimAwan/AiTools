package com.ght666.aiTools.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * @author: 1012ght
 */
@Configuration
@Slf4j
public class RabbitMQConfig {

    public static final String PDF_UPLOAD_QUEUE = "pdf.upload.queue";
    public static final String PDF_PROCESS_QUEUE = "pdf.process.queue";
    public static final String PDF_RESULT_QUEUE = "pdf.result.queue";
    public static final String PDF_DLX_QUEUE = "pdf.dlx.queue";
    public static final String PDF_EXCHANGE = "pdf.exchange";
    public static final String PDF_RESULT_EXCHANGE = "pdf.result.exchange";
    // 队列
    @Bean
    public Queue pdfUploadQueue(){
        return QueueBuilder.durable(PDF_UPLOAD_QUEUE)
                .withArgument("x-dead-letter-exchange",PDF_EXCHANGE)    //死信交换机
                .withArgument("x-dead-letter-routing-key","pdf.dlx")    //指定路由键
                .build();
    }
    @Bean
    public Queue pdfProcessQueue(){
        return QueueBuilder.durable(PDF_PROCESS_QUEUE)
                .withArgument("x-message-ttl", 300000)
                .withArgument("x-dead-letter-exchange",PDF_EXCHANGE)
                .withArgument("x-dead-letter-routing-key","pdf.dlx")
                .build();
    }
    @Bean
    public Queue pdfResultQueue(){
        return QueueBuilder.durable(PDF_RESULT_QUEUE)
                .build();
    }
    @Bean
    public Queue pdfDlxQueue() {
        return QueueBuilder.durable(PDF_DLX_QUEUE).build();
    }

    // 交换机
    @Bean
    public DirectExchange pdfExchange() {
        return new DirectExchange(PDF_EXCHANGE);
    }
    @Bean
    public DirectExchange pdfResultExchange() {
        return new DirectExchange(PDF_RESULT_EXCHANGE);
    }
    // 绑定
    @Bean
    public Binding pdfUploadBinding() {
        return BindingBuilder.bind(pdfUploadQueue())
                .to(pdfExchange())
                .with("pdf.upload");
    }
    @Bean
    public Binding pdfProcessBinding() {
        return BindingBuilder.bind(pdfProcessQueue())
                .to(pdfExchange())
                .with("pdf.process");
    }
    @Bean
    public Binding pdfResultBinding() {
        return BindingBuilder.bind(pdfResultQueue())
                .to(pdfResultExchange())
                .with("pdf.result");
    }
    @Bean
    public Binding pdfDlxBinding() {
        return BindingBuilder.bind(pdfDlxQueue())
                .to(pdfExchange())
                .with("pdf.dlx");
    }
    // 消息转换器
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    // 连接工厂
    @Bean
    public ConnectionFactory connnectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }
    // RabbitTemplate配置
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.error("消息发送失败: {}", cause);
            }
        });
        return  template;
    }
}
