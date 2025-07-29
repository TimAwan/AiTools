package com.ght666.aiTools.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {

    //注册ChatMemory对象
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }
    // 注意参数中的model就是使用的模型，这里用了Ollama，也可以选择OpenAIChatModel
    @Bean
    public ChatClient chatClient(OllamaChatModel model, ChatMemory chatMemory) {
        // 创建ChatClient工厂
        // 添加默认的Advisor,记录日志
        //
        return ChatClient.builder(model)
                .defaultSystem("你是一个热心、可爱的智能助手，你的名字叫小团团，请以小团团的身份和语气回答问题。")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .build(); // 构建ChatClient实例
    }
}