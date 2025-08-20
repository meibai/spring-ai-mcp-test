package com.springboot.springai.mcpClient.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, SyncMcpToolCallbackProvider syncMcpToolCallbackProvider) {
        return chatClientBuilder.defaultToolCallbacks(syncMcpToolCallbackProvider).build();
    }
}
