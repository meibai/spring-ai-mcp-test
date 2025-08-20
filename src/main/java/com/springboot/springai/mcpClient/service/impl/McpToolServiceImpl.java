package com.springboot.springai.mcpClient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.springai.mcpClient.entity.McpTool;
import com.springboot.springai.mcpClient.entity.McpServer;
import com.springboot.springai.mcpClient.mapper.McpToolMapper;
import com.springboot.springai.mcpClient.service.McpToolService;
import com.springboot.springai.mcpClient.service.McpServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class McpToolServiceImpl extends ServiceImpl<McpToolMapper, McpTool> implements McpToolService {
    
    @Autowired
    @Lazy
    private McpServerService mcpServerService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public boolean fetchAndSaveTools(String serverCode) {
        try {
            // 获取指定的MCP Server
            McpServer server = mcpServerService.getOne(new QueryWrapper<McpServer>().eq("code", serverCode));
            if (server == null) {
                return false;
            }
            
            // 解析服务器配置获取连接信息
            JsonNode configNode = objectMapper.readTree(server.getConfigJson());
            
            // 调用MCP Server的接口获取Tool信息
            // 这里应该实际调用MCP Server的listTools接口
            // 暂时使用模拟数据进行演示
            
            // 根据服务器类型处理不同逻辑
            String serverType = configNode.has("type") ? configNode.get("type").asText() : "unknown";

            // 模拟获取工具数据的逻辑
            for (int i = 1; i <= 3; i++) {
                String toolCode = serverCode + "_tool_" + i;
                
                // 检查工具是否已存在
                McpTool tool = this.getOne(new QueryWrapper<McpTool>()
                    .eq("code", toolCode)
                    .eq("server_code", serverCode));
                
                if (tool == null) {
                    tool = new McpTool();
                    tool.setCode(toolCode);
                    tool.setServerCode(serverCode);
                    tool.setName("Tool " + i + " for " + serverCode);
                    tool.setDescription("Description for tool " + i + " on server " + serverCode);
                    
                    // 构建输入模式
                    String inputSchema = "{\n" +
                            "  \"type\": \"object\",\n" +
                            "  \"properties\": {\n" +
                            "    \"param" + i + "\": {\n" +
                            "      \"type\": \"string\",\n" +
                            "      \"description\": \"Parameter " + i + " for tool " + toolCode + "\"\n" +
                            "    }\n" +
                            "  }\n" +
                            "}";
                    tool.setInputSchema(inputSchema);
                    
                    tool.setEnable(false);
                    tool.setStatus("off");
                    tool.setCreateTime(LocalDateTime.now());
                }
                
                tool.setUpdateTime(LocalDateTime.now());
                this.saveOrUpdate(tool);
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void fetchAndSaveAllTools() {
        // TODO: 实现获取所有MCP Server的Tool并保存到数据库
        List<McpServer> servers = mcpServerService.getAllServers();
        for (McpServer server : servers) {
            fetchAndSaveTools(server.getCode());
        }
    }
    
    @Override
    public void checkAllToolHealth() {
        List<McpTool> tools = this.list();
        for (McpTool tool : tools) {
            // 检查每个工具的健康状态
            String status = checkToolHealth(tool.getCode());
            tool.setStatus(status);
            tool.setCheckTime(LocalDateTime.now());
            this.updateById(tool);
        }
    }
    
    @Override
    public String checkToolHealth(String toolCode) {
        McpTool tool = this.getOne(new QueryWrapper<McpTool>().eq("code", toolCode));
        if (tool != null) {
            // 检查工具健康状态
            // 这里应该实际调用MCP Server的工具健康检查接口
            // 暂时返回模拟状态
            return "on";
        }
        return "off";
    }
    
    @Override
    public List<McpTool> getToolsByServerCode(String serverCode) {
        return this.list(new QueryWrapper<McpTool>().eq("server_code", serverCode));
    }
    
    @Override
    public boolean enableTool(String toolCode) {
        McpTool tool = this.getOne(new QueryWrapper<McpTool>().eq("code", toolCode));
        if (tool != null) {
            tool.setEnable(true);
            // 调用MCP Server的enableTool接口
            // 这里应该实际调用MCP Server的enableTool接口
            // 暂时直接更新数据库
            tool.setUpdateTime(LocalDateTime.now());
            return this.updateById(tool);
        }
        return false;
    }
    
    @Override
    public boolean disableTool(String toolCode) {
        McpTool tool = this.getOne(new QueryWrapper<McpTool>().eq("code", toolCode));
        if (tool != null) {
            tool.setEnable(false);
            // 调用MCP Server的disableTool接口
            // 这里应该实际调用MCP Server的disableTool接口
            // 暂时直接更新数据库
            tool.setUpdateTime(LocalDateTime.now());
            return this.updateById(tool);
        }
        return false;
    }
    
    @Override
    public String testTool(String toolCode, String inputParameters) {
        // 测试指定Tool
        // 需要调用MCP Server的testTool接口进行测试
        // 这里应该实际调用MCP Server的工具测试接口
        // 暂时返回模拟结果
        try {
            // 验证输入参数格式
            if (inputParameters != null && !inputParameters.isEmpty()) {
                JsonNode paramNode = objectMapper.readTree(inputParameters);
                // 验证参数是否符合工具的输入schema
                // 这里可以添加更复杂的验证逻辑
            }
            
            // 模拟测试结果
            return "Test result for tool: " + toolCode + " with parameters: " + inputParameters;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error testing tool: " + toolCode + ", error: " + e.getMessage();
        }
    }
}