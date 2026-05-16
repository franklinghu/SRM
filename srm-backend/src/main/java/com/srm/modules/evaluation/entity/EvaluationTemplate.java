package com.srm.modules.evaluation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("evaluation_template")
public class EvaluationTemplate {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String templateName;
    private String description;
    private String criteria;
    private Integer status;
    private Long tenantId;
    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
