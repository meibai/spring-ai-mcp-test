package com.springboot.springai.examples.service;

public interface AssistantService {
    String chat(String input);

    String chatMemory(String chatId, String input);

    String chatMemoryWithFunctionCalling(String chatId, String input);

    void clearChat(String chatId);
}