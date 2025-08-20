package com.springboot.springai.mcpClient.task;

import com.springboot.springai.mcpClient.service.McpServerService;
import com.springboot.springai.mcpClient.service.McpToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HealthCheckTask {
    
    @Autowired
    private McpServerService mcpServerService;
    
    @Autowired
    private McpToolService mcpToolService;
    
    /**
     * 定时检查所有MCP Server的健康状态
     * 每30分钟执行一次
     * 对应需求3.1: 定时获取所有的tb_mcp_server，检查mcp server 的健康状态，
     * 并更新到tb_mcp_server中status字段和check_time字段
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void checkAllServerHealth() {
        mcpServerService.checkAllServerHealth();
    }
    
    /**
     * 定时检查所有Tool的健康状态
     * 每30分钟执行一次
     * 对应需求3.2: 依次调用mcp server定时获取所有的tool，调用gettoolinfo接口获取tool详情信息，
     * 保存或者更新到tb_mcp_tool中，并检查所有tool的健康状态
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void checkAllToolHealth() {
        mcpToolService.checkAllToolHealth();
    }
    
    /**
     * 定时获取所有Tool信息并更新
     * 每小时执行一次
     * 对应需求3.2: 依次调用mcp server定时获取所有的tool，调用gettoolinfo接口获取tool详情信息，
     * 保存或者更新到tb_mcp_tool中
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void fetchAndSaveAllTools() {
        mcpToolService.fetchAndSaveAllTools();
    }
}