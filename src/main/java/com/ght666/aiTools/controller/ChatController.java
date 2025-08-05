package com.ght666.aiTools.controller;

import com.ght666.aiTools.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.model.Media;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

/**
 * @author 1012ght
 */
@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;

    private final ChatMemory chatMemory;

    private final ChatHistoryRepository chatHistoryRepository;
    // chat
    /*@RequestMapping("/chat")
    public String chat(@RequestParam(defaultValue = "讲个笑话") String prompt) {
        return chatClient
                .prompt(prompt) // 传入user提示词
                .call() // 同步请求，会等待AI全部输出完才返回结果
                .content(); //返回响应内容
    }*/
    // 改为流式调用 且多模态
    // 注意看返回值，是Flux<String>，也就是流式结果，另外需要设定响应类型和编码，不然前端会乱码
    @RequestMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(
            @RequestParam("prompt") String prompt,
            @RequestParam("chatId") String chatId,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        // 保存会话id
        chatHistoryRepository.save("chat", chatId);
        // 请求模型
        if (files == null || files.isEmpty()) {
            // 没有附件，纯文本聊天
            return textChat(prompt, chatId);
        } else {
            // 有附件，多模态聊天
            return multiModalChat(prompt, chatId, files);
        }

    }
    private Flux<String> multiModalChat(String prompt, String chatId, List<MultipartFile> files) {
        // 解析多媒体
        List<Media> medias = files.stream()
                .map(file -> new Media(
                                MimeType.valueOf(Objects.requireNonNull(file.getContentType())),
                                file.getResource()
                        )
                )
                .toList();
        // 请求模型
        return chatClient.prompt()
                .user(p -> p.text(prompt).media(medias.toArray(Media[]::new)))
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .stream()
                .content();
    }

    private Flux<String> textChat(String prompt, String chatId) {
        return chatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .stream()
                .content();
    }
}