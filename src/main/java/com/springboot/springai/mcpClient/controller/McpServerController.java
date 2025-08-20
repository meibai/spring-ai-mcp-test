
// 修改 McpServerController.java
package com.springboot.springai.mcpClient.controller;

import com.springboot.springai.mcpClient.entity.McpServer;
import com.springboot.springai.mcpClient.entity.McpTool;
import com.springboot.springai.mcpClient.service.McpServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mcp/server")
@Tag(name = "MCP服务器管理", description = "管理MCP服务器的相关接口")
public class McpServerController {

    @Autowired
    private McpServerService mcpServerService;

    /**
     * 导入MCP Server信息
     * @param serverJson JSON格式的服务器信息
     * @return 是否导入成功
     */
    @PostMapping("/import")
    @Operation(summary = "导入MCP Server信息", description = "通过JSON格式导入单个MCP服务器信息")
    public boolean importMcpServer(@Parameter(description = "JSON格式的服务器信息") @RequestBody String serverJson) {
        return mcpServerService.importMcpServerFromJson(serverJson);
    }

    /**
     * 批量导入MCP Server信息
     * @param serverJsonList JSON格式的服务器信息列表
     * @return 是否导入成功
     */
    @PostMapping("/import/batch")
    @Operation(summary = "批量导入MCP Server信息", description = "通过JSON格式批量导入多个MCP服务器信息")
    public boolean importMcpServers(@Parameter(description = "JSON格式的服务器信息列表") @RequestBody List<String> serverJsonList) {
        return mcpServerService.importMcpServersFromJson(serverJsonList);
    }

    /**
     * 查询所有MCP Server列表
     * @return MCP Server列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有MCP Server列表", description = "获取所有已配置的MCP服务器列表")
    public List<McpServer> getAllServers() {
        return mcpServerService.getAllServers();
    }

    /**
     * 即时检查指定MCP Server的健康状态
     * @param serverCode 服务器代码
     * @return 服务器状态
     */
    @GetMapping("/health/{serverCode}")
    @Operation(summary = "检查MCP Server健康状态", description = "即时检查指定MCP服务器的健康状态")
    public String checkServerHealth(@Parameter(description = "服务器代码") @PathVariable String serverCode) {
        return mcpServerService.checkServerHealth(serverCode);
    }

    /**
     * 手动触发获取指定MCP Server的所有Tool
     * @param serverCode 服务器代码
     * @return 是否获取成功
     */
    @PostMapping("/fetch-tools/{serverCode}")
    @Operation(summary = "获取指定MCP Server的所有Tool", description = "手动触发获取指定MCP服务器的所有工具")
    public boolean fetchTools(@Parameter(description = "服务器代码") @PathVariable String serverCode) {
        return mcpServerService.fetchToolsByServerCode(serverCode);
    }

    /**
     * 手动触发获取所有MCP Server的Tool
     * @return 所有工具列表
     */
    @PostMapping("/fetch-all-tools")
    @Operation(summary = "获取所有MCP Server的Tool", description = "手动触发获取所有MCP服务器的工具")
    public boolean fetchAllTools() {
        return mcpServerService.fetchAllTools();
    }
}