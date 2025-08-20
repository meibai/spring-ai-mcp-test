package com.springboot.springai.mcpClient.service.impl;


import com.springboot.springai.mcpClient.service.McpClientService;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class McpClientServiceImpl implements McpClientService {

    @Autowired
    private List<McpSyncClient> mcpSyncClients;


    @Autowired
    private ChatClient chatClient;



    @Override
    public List<McpSchema.Tool> getAllTools() {
        ArrayList<McpSchema.Tool> allTools = new ArrayList<>();
        for (McpSyncClient mcpSyncClient : mcpSyncClients) {
            allTools.addAll(mcpSyncClient.listTools().tools());
        }
        return allTools;
    }

    @Override
    public String chatWithMcp(String prompt) {
        return chatClient.prompt(prompt).call().content();
    }

}
