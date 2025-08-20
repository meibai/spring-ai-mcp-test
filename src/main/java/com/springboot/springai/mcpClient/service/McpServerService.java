package com.springboot.springai.mcpClient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.springai.mcpClient.entity.McpServer;
import com.springboot.springai.mcpClient.entity.McpTool;

import java.util.List;

public interface McpServerService extends IService<McpServer> {
    
    /**
     * 通过JSON导入MCP Server信息
     * @param serverJson JSON格式的服务器信息
     * @return 是否导入成功
     */
    boolean importMcpServerFromJson(String serverJson);
    
    /**
     * 批量导入MCP Server信息
     * @param serverJsonList JSON格式的服务器信息列表
     * @return 是否导入成功
     */
    boolean importMcpServersFromJson(List<String> serverJsonList);
    
    /**
     * 检查所有MCP Server的健康状态
     */
    void checkAllServerHealth();
    
    /**
     * 即时检查指定MCP Server的健康状态
     * @param serverCode 服务器代码
     * @return 服务器状态
     */
    String checkServerHealth(String serverCode);
    
    /**
     * 获取所有MCP Server列表
     * @return MCP Server列表
     */
    List<McpServer> getAllServers();
    
    /**
     * 获取指定MCP Server的所有Tool
     * @param serverCode 服务器代码
     * @return 是否获取成功
     */
    boolean fetchToolsByServerCode(String serverCode);
    
    /**
     * 获取所有MCP Server的Tool
     */
    boolean fetchAllTools();
}