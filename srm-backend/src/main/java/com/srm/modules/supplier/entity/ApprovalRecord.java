package com.srm.modules.supplier.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("approval_record")
public class ApprovalRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long applicationId;
    private Long stepId;
    private Integer stepNo;
    private String stepName;
    private Long approverId;
    private String approverName;
    private String action;
    private String comment;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
