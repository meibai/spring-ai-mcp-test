package com.springboot.springai.mcpClient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.springai.mcpClient.entity.McpTool;
import com.springboot.springai.mcpClient.entity.McpServer;

import java.util.List;

public interface McpToolService extends IService<McpTool> {
    
    /**
     * 获取指定MCP Server的所有Tool并保存到数据库
     * @param serverCode 服务器代码
     * @return 是否获取成功
     */
    boolean fetchAndSaveTools(String serverCode);
    
    /**
     * 获取所有MCP Server的Tool并保存到数据库
     */
    void fetchAndSaveAllTools();
    
    /**
     * 检查所有Tool的健康状态
     */
    void checkAllToolHealth();
    
    /**
     * 即时检查指定Tool的健康状态
     * @param toolCode 工具代码
     * @return 工具状态
     */
    String checkToolHealth(String toolCode);
    
    /**
     * 获取指定MCP Server的所有Tool
     * @param serverCode 服务器代码
     * @return Tool列表
     */
    List<McpTool> getToolsByServerCode(String serverCode);
    
    /**
     * 启用指定Tool
     * @param toolCode 工具代码
     * @return 是否启用成功
     */
    boolean enableTool(String toolCode);
    
    /**
     * 禁用指定Tool
     * @param toolCode 工具代码
     * @return 是否禁用成功
     */
    boolean disableTool(String toolCode);
    
    /**
     * 测试指定Tool
     * @param toolCode 工具代码
     * @param inputParameters 输入参数
     * @return 测试结果
     */
    String testTool(String toolCode, String inputParameters);
}