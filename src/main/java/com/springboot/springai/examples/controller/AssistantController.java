package com.springboot.springai.examples.controller;

import com.springboot.springai.examples.service.AssistantService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacherAssistant")
@Validated
public class AssistantController {
    private final AssistantService assistantService;

    @Autowired
    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam @NotBlank(message = "Input Query is required") String question) {
        return ResponseEntity.ok(assistantService.chat(question));
    }

    @GetMapping("/chat/memory")
    public ResponseEntity<String> chatMemory(
            @RequestParam @NotBlank(message = "Chat ID is required") String chatId,
            @RequestParam @NotBlank(message = "Input Query is required") String question
        ) {
        return ResponseEntity.ok(assistantService.chatMemory(chatId, question));
    }

    @GetMapping("/chat/memory/functionCalling")
    public ResponseEntity<String> chatMemoryWithFunctionCalling(
            @RequestParam @NotBlank(message = "Chat ID is required") String chatId,
            @RequestParam @NotBlank(message = "Input Query is required") String question
    ) {
        return ResponseEntity.ok(assistantService.chatMemoryWithFunctionCalling(chatId, question));
    }

    @GetMapping("/clearChat")
    public ResponseEntity<Void> clearChat(
            @RequestParam @NotBlank(message = "Chat ID is required") String chatId
    ) {
        assistantService.clearChat(chatId);
        return ResponseEntity.noContent().build();
    }
}