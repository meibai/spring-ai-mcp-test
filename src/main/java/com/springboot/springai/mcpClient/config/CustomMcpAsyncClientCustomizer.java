package com.springboot.springai.mcpClient.config;

import io.modelcontextprotocol.client.McpClient;
import org.springframework.ai.mcp.customizer.McpAsyncClientCustomizer;
import org.springframework.stereotype.Component;

import java.time.Duration;

//@Component
public class CustomMcpAsyncClientCustomizer implements McpAsyncClientCustomizer {
    @Override
    public void customize(String name, McpClient.AsyncSpec spec) {
        spec.requestTimeout(Duration.ofSeconds(30));


    }
}
