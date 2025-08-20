package com.springboot.springai.mcpClient.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_mcp_server")
public class McpServer {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String code;
    
    private String name;
    
    private String description;
    
    private String configJson;
    
    private Boolean enable;
    
    private String status;
    
    private LocalDateTime checkTime;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}