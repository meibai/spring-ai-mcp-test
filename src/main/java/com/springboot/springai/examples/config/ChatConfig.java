package com.springboot.springai.examples.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class ChatConfig {
    @Bean
    public ChatMemory chatMemory() {
        // 1. InMemoryChatMemory
        // 2. RedisChatMemory
        // 3. Database-Backend
        return new InMemoryChatMemory();
    }

    @Bean
    public OpenAiChatOptions teacherAssistantChatOptions() {
        return OpenAiChatOptions.builder()
//                .model("gpt-4o")
                .temperature(0.5)
                .maxTokens(750)
                .frequencyPenalty(0.2)
                .functions(Set.of("operation1", "operation2"))
                .build();
    }

    @Bean
    public String teacherAssistantPromptTemplate() {
        return """
                    You are a highly intelligent and patient teaching assistant.
                    Your role is to explain concepts clearly, answer questions concisely, and provide examples for better understanding.
                    Always adapt your response to the user's level of knowledge.

                    Query: %s
                    Difficulty level: %s

                    Provide a clear, structured explanation and use examples when helpful.
                """;
    }

    @Bean
    public String teacherAssistantPromptTemplateNew() {
        return """
            You are a highly intelligent and patient teaching assistant.
            Your role is to explain concepts clearly, answer questions concisely, and provide examples for better understanding.
            Always adapt your response to the user's level of knowledge.
            
            Instructions:
            1. If the query involves actions, operations, or dynamic interactions or something similar:
               - Call the appropriate functions (like "operation1" or "operation2").
               - Respond with a concise message only in this format: "The operation [operation name] has been executed!"
               - No need to add more text or message.
            2. For all other queries:
               - Provide a clear, structured explanation.
               - Include examples when helpful to enhance understanding.

            Query: %s
            Difficulty Level: %s
            """;
    }

    @Bean
    public String mathAssistantPromptTemplate() {
        return """
               """;
    }
}