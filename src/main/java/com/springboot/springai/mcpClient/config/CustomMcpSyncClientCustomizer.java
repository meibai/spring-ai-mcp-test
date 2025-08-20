package com.springboot.springai.mcpClient.config;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.customizer.McpSyncClientCustomizer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 *  Customize the McpSyncClient
 */
@Slf4j
@Component
public class CustomMcpSyncClientCustomizer implements McpSyncClientCustomizer {

    @Override
    public void customize(String serverConfigurationName, McpClient.SyncSpec spec) {
// Customize the request timeout configuration
        spec.requestTimeout(Duration.ofSeconds(30));
        log.info("******    {}",serverConfigurationName);

        // Sets the root URIs that this client can access.
        // todo
        spec.roots(Collections.emptyList());

        // Sets a custom sampling handler for processing message creation requests.
        spec.sampling((McpSchema.CreateMessageRequest messageRequest) -> {
            // todo Handle sampling
            McpSchema.CreateMessageResult result = new McpSchema.CreateMessageResult(null, null, null, null);
            return result;
        });

        // Adds a consumer to be notified when the available tools change, such as tools
        // being added or removed.
        spec.toolsChangeConsumer((List<McpSchema.Tool> tools) -> {
            tools.forEach(tool -> log.info(tool.toString()));
            // Handle tools change
        });

        // Adds a consumer to be notified when the available resources change, such as resources
        // being added or removed.
        spec.resourcesChangeConsumer((List<McpSchema.Resource> resources) -> {
            // Handle resources change
            resources.forEach(resource -> log.info(resource.toString()));
        });

        // Adds a consumer to be notified when the available prompts change, such as prompts
        // being added or removed.
        spec.promptsChangeConsumer((List<McpSchema.Prompt> prompts) -> {
            // Handle prompts change
            prompts.forEach(prompt -> log.info(prompt.toString()));
        });

        // Adds a consumer to be notified when logging messages are received from the server.
        spec.loggingConsumer((McpSchema.LoggingMessageNotification logger) -> {
            // Handle log messages
            log.info("{},{},{}", logger.level(), logger.logger(), logger.data());
        });
    }
}
