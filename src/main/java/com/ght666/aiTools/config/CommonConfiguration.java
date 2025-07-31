package com.ght666.aiTools.config;

import com.ght666.aiTools.tools.CourseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static com.ght666.aiTools.constants.SystemConstants.CUSTOMER_SERVICE_SYSTEM;
import static com.ght666.aiTools.constants.SystemConstants.GAME_SYSTEM_PROMPT;

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

    //客服聊天模块的配置
    @Bean
    public ChatClient serviceChatClient(
            OpenAiChatModel model,
            ChatMemory chatMemory,
            CourseTools courseTools) {
        return ChatClient.builder(model)
                .defaultSystem(CUSTOMER_SERVICE_SYSTEM)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory), // CHAT MEMORY
                        new SimpleLoggerAdvisor())
                .defaultTools(courseTools)
                .build();
    }

    //向量存储
    @Bean
    public VectorStore vectorStore(OpenAiEmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    //PDF
    @Bean
    public ChatClient pdfChatClient(
            OpenAiChatModel model,
            ChatMemory chatMemory,
            VectorStore vectorStore) {
        return ChatClient.builder(model)
                .defaultSystem("请根据提供的上下文回答问题，不要自己猜测。")
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory), // CHAT MEMORY
                        new SimpleLoggerAdvisor(),
                        new QuestionAnswerAdvisor(
                                vectorStore, // 向量库
                                SearchRequest.builder() // 向量检索的请求参数
                                        .similarityThreshold(0.5d) // 相似度阈值
                                        .topK(2) // 返回的文档片段数量
                                        .build()
                        )
                )
                .build();
    }
}