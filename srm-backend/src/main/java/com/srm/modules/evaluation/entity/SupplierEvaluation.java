package com.srm.modules.evaluation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("supplier_evaluation")
public class SupplierEvaluation {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long supplierId;
    private Long templateId;
    private String evaluationNo;
    private String evaluationPeriod;
    private LocalDate evaluationDate;
    private BigDecimal totalScore;
    private String level;
    private String evaluator;
    private String remarks;
    private String status;
    private Long tenantId;
    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
