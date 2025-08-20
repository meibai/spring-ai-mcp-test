package com.springboot.springai.mcpClient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.springai.mcpClient.entity.McpServer;
import com.springboot.springai.mcpClient.mapper.McpServerMapper;
import com.springboot.springai.mcpClient.service.McpServerService;
import com.springboot.springai.mcpClient.service.McpToolService;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class McpServerServiceImpl extends ServiceImpl<McpServerMapper, McpServer> implements McpServerService {
    
    private static final Logger log = LoggerFactory.getLogger(McpServerServiceImpl.class);
    
    @Autowired
    @Lazy
    private McpToolService mcpToolService;
    
    @Autowired
    private List<McpSyncClient> mcpSyncClients;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public boolean importMcpServerFromJson(String serverJson) {
        try {
            // 解析JSON并保存到数据库
            JsonNode rootNode = objectMapper.readTree(serverJson);
            
            List<McpServer> serversToSave = new ArrayList<>();
            
            // 处理mcpServers对象中的服务
            if (rootNode.has("mcpServers")) {
                JsonNode mcpServersNode = rootNode.get("mcpServers");
                Iterator<Map.Entry<String, JsonNode>> fields = mcpServersNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    String serverCode = entry.getKey();
                    JsonNode serverNode = entry.getValue();
                    
                    McpServer server = new McpServer();
                    server.setCode(serverCode);
                    server.setName(serverCode); // 默认使用code作为name
                    
                    // 构建config_json
                    server.setConfigJson(serverNode.toString());
                    
                    server.setEnable(true); // 默认启用
                    server.setStatus("off"); // 默认状态为off，需要检查后更新
                    server.setCreateTime(LocalDateTime.now());
                    server.setUpdateTime(LocalDateTime.now());
                    
                    serversToSave.add(server);
                }
            }
            
            // 处理根级别其他服务（如filesystem）
            Iterator<Map.Entry<String, JsonNode>> rootFields = rootNode.fields();
            while (rootFields.hasNext()) {
                Map.Entry<String, JsonNode> entry = rootFields.next();
                String serverCode = entry.getKey();
                
                // 跳过mcpServers字段，因为它已经被处理过了
                if (!"mcpServers".equals(serverCode)) {
                    JsonNode serverNode = entry.getValue();
                    
                    // 检查是否已存在相同code的服务器
                    boolean exists = false;
                    for (McpServer existingServer : serversToSave) {
                        if (existingServer.getCode().equals(serverCode)) {
                            exists = true;
                            break;
                        }
                    }
                    
                    if (!exists) {
                        McpServer server = new McpServer();
                        server.setCode(serverCode);
                        server.setName(serverCode); // 默认使用code作为name
                        
                        // 构建config_json
                        server.setConfigJson(serverNode.toString());
                        
                        server.setEnable(true); // 默认启用
                        server.setStatus("off"); // 默认状态为off，需要检查后更新
                        server.setCreateTime(LocalDateTime.now());
                        server.setUpdateTime(LocalDateTime.now());
                        
                        serversToSave.add(server);
                    }
                }
            }
            
            // 保存到数据库
            for (McpServer server : serversToSave) {
                // 检查是否已存在相同code的服务器
                McpServer existingServer = this.getOne(new QueryWrapper<McpServer>().eq("code", server.getCode()));
                if (existingServer != null) {
                    // 更新现有服务器
                    server.setId(existingServer.getId());
                    server.setUpdateTime(LocalDateTime.now());
                    this.updateById(server);
                } else {
                    // 保存新服务器
                    this.save(server);
                }
            }
            
            return true;
        } catch (Exception e) {
            // 记录日志
            log.error("导入MCP服务器配置失败", e);
            return false;
        }
    }
    
    @Override
    public boolean importMcpServersFromJson(List<String> serverJsonList) {
        boolean result = true;
        for (String serverJson : serverJsonList) {
            result = result && importMcpServerFromJson(serverJson);
        }
        return result;
    }
    
    @Override
    public void checkAllServerHealth() {
        List<McpServer> servers = this.list();
        for (McpServer server : servers) {
            // 检查每个服务器的健康状态
            String status = checkServerHealth(server.getCode());
            server.setStatus(status);
            server.setCheckTime(LocalDateTime.now());
            this.updateById(server);
        }
    }
    
    @Override
    public String checkServerHealth(String serverCode) {
        McpServer server = this.getOne(new QueryWrapper<McpServer>()
                .eq("code", serverCode));
        if (server != null) {
            try {
                // 尝试通过MCP客户端检查服务器健康状态
                // 查找对应的MCP客户端
                for (McpSyncClient client : mcpSyncClients) {
                    try {
                        // 尝试列出工具来检查连接是否正常
                        McpSchema.ListToolsResult result = client.listTools();
                        if (result != null) {
                            log.info("服务器 {} 健康检查成功", serverCode);
                            return "on";
                        }
                    } catch (Exception e) {
                        log.warn("服务器 {} 健康检查失败: {}", serverCode, e.getMessage());
                    }
                }
            } catch (Exception e) {
                log.error("检查服务器 {} 健康状态时发生错误: {}", serverCode, e.getMessage());
            }
            return "off";
        }
        return "off";
    }
    
    @Override
    public List<McpServer> getAllServers() {
        return this.list();
    }
    
    @Override
    public boolean fetchToolsByServerCode(String serverCode) {
        return mcpToolService.fetchAndSaveTools(serverCode);
    }
    
    @Override
    public boolean fetchAllTools() {
        mcpToolService.fetchAndSaveAllTools();
        return true;
    }
}