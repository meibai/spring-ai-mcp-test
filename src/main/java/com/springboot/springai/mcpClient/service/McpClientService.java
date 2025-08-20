package com.springboot.springai.mcpClient.service;

import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;

public interface McpClientService {
    List<McpSchema.Tool> getAllTools();

    String chatWithMcp(String prompt);
}
