package com.ght666.aiTools.controller;

import com.ght666.aiTools.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

/**
 * 赞奇科技客服控制器
 */
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ZanqiCustomerController {

    private final ChatClient zanqiChatClient;
    private final ChatHistoryRepository chatHistoryRepository;

    @RequestMapping(value = "/zanqi", produces = "text/html;charset=utf-8")
    public Flux<String> zanqi(String prompt, String chatId) {
        // 1.保存会话id
        chatHistoryRepository.save("zanqi", chatId);
        // 2.请求模型
        return zanqiChatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .stream()
                .content();
    }
}