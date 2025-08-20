package com.springboot.springai.mcpClient.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_mcp_tool")
public class McpTool {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String code;
    
    private String serverCode;
    
    private String name;
    
    private String description;
    
    private String inputSchema;
    
    private Boolean enable;
    
    private String status;
    
    private LocalDateTime checkTime;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    

}