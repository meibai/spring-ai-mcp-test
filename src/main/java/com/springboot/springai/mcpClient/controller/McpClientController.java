
package com.springboot.springai.mcpClient.controller;

import com.springboot.springai.mcpClient.service.McpClientService;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
@RequestMapping("/mcpClient")
@Tag(name = "MCP客户端", description = "MCP客户端相关接口")
public class McpClientController {

    @Autowired
    private List<McpSyncClient> mcpSyncClients;
    @Autowired
    private McpClientService mcpClientService;

    @RequestMapping("/getAllToolsTest")
    @Operation(summary = "获取所有工具(测试)", description = "测试接口，获取所有可用工具")
    public List<McpSchema.Tool> getAllToolsTest() {
        return mcpClientService.getAllTools();
    }

    @GetMapping("/getAllTools")
    @Operation(summary = "获取所有工具", description = "通过MCP客户端获取所有工具")
    public Mono<ResponseEntity<McpSchema.ListToolsResult>> getAllTools() {
        return Mono.fromCallable(() -> mcpSyncClients.get(0).listTools())
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).build());
    }

    @GetMapping("/chatWithMcp")
    @Operation(summary = "与MCP服务器对话", description = "使用MCP协议与服务器进行对话")
    public String chatWithMcp(String prompt) {
        return mcpClientService.chatWithMcp(prompt);
    }
}