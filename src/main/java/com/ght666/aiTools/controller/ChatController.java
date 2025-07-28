package com.ght666.aiTools.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;
    // chat
    /*@RequestMapping("/chat")
    public String chat(@RequestParam(defaultValue = "讲个笑话") String prompt) {
        return chatClient
                .prompt(prompt) // 传入user提示词
                .call() // 同步请求，会等待AI全部输出完才返回结果
                .content(); //返回响应内容
    }*/
    //改为流式调用
    // 注意看返回值，是Flux<String>，也就是流式结果，另外需要设定响应类型和编码，不然前端会乱码
    @RequestMapping(value = "/chat", produces = "text/html;charset=UTF-8")
    public Flux<String> chat(@RequestParam(defaultValue = "讲个笑话") String prompt) {
        return chatClient
                .prompt(prompt)
                .stream() // 流式调用
                .content();
    }
}