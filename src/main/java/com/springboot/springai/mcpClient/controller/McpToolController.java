
// 修改 McpToolController.java
package com.springboot.springai.mcpClient.controller;

import com.springboot.springai.mcpClient.entity.McpTool;
import com.springboot.springai.mcpClient.service.McpToolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mcp/tool")
@Tag(name = "MCP工具管理", description = "管理MCP工具的相关接口")
public class McpToolController {

    @Autowired
    private McpToolService mcpToolService;

    /**
     * 获取指定MCP Server的所有Tool
     * @param serverCode 服务器代码
     * @return Tool列表
     */
    @GetMapping("/list/{serverCode}")
    @Operation(summary = "获取指定MCP Server的所有Tool", description = "根据服务器代码获取该服务器上所有可用的工具列表")
    public List<McpTool> getToolsByServer(@Parameter(description = "服务器代码") @PathVariable String serverCode) {
        return mcpToolService.getToolsByServerCode(serverCode);
    }

    /**
     * 启用指定Tool
     * @param toolCode 工具代码
     * @return 是否启用成功
     */
    @PostMapping("/enable/{toolCode}")
    @Operation(summary = "启用指定Tool", description = "启用指定的工具，使其可以在AI对话中使用")
    public boolean enableTool(@Parameter(description = "工具代码") @PathVariable String toolCode) {
        return mcpToolService.enableTool(toolCode);
    }

    /**
     * 禁用指定Tool
     * @param toolCode 工具代码
     * @return 是否禁用成功
     */
    @PostMapping("/disable/{toolCode}")
    @Operation(summary = "禁用指定Tool", description = "禁用指定的工具，使其不能在AI对话中使用")
    public boolean disableTool(@Parameter(description = "工具代码") @PathVariable String toolCode) {
        return mcpToolService.disableTool(toolCode);
    }

    /**
     * 查看Tool详情
     * @param toolCode 工具代码
     * @return Tool详情
     */
    @GetMapping("/detail/{toolCode}")
    @Operation(summary = "查看Tool详情", description = "获取指定工具的详细信息")
    public McpTool getToolDetail(@Parameter(description = "工具代码") @PathVariable String toolCode) {
        return mcpToolService.getById(toolCode);
    }

    /**
     * 测试指定Tool
     * @param toolCode 工具代码
     * @param inputParameters 输入参数
     * @return 测试结果
     */
    @PostMapping("/test/{toolCode}")
    @Operation(summary = "测试指定Tool", description = "测试指定工具的功能")
    public String testTool(@Parameter(description = "工具代码") @PathVariable String toolCode,
                           @Parameter(description = "输入参数") @RequestBody String inputParameters) {
        return mcpToolService.testTool(toolCode, inputParameters);
    }

    /**
     * 即时检查指定Tool的健康状态
     * @param toolCode 工具代码
     * @return 工具状态
     */
    @GetMapping("/health/{toolCode}")
    @Operation(summary = "检查Tool健康状态", description = "即时检查指定工具的健康状态")
    public String checkToolHealth(@Parameter(description = "工具代码") @PathVariable String toolCode) {
        return mcpToolService.checkToolHealth(toolCode);
    }

    /**
     * 手动触发获取所有Tool信息
     * @return 是否执行成功
     */
    @PostMapping("/fetch-all")
    @Operation(summary = "获取所有Tool信息", description = "手动触发获取并保存所有工具信息")
    public boolean fetchAllTools() {
        mcpToolService.fetchAndSaveAllTools();
        return true;
    }
}