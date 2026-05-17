package com.srm.modules.supplier.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("approval_step")
public class ApprovalStep {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long flowId;
    private Integer stepNo;
    private String stepName;
    private String roleCode;
    private Long userId;
    private Integer isRequired;
    private Integer isParallel;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
