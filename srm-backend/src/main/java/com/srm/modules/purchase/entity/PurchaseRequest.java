package com.srm.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("purchase_request")
public class PurchaseRequest {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String requestNo;
    private String title;
    private String requestType;
    private String department;
    private Long requesterId;
    private LocalDate expectedDate;
    private BigDecimal totalAmount;
    private Integer priority;
    private String reason;
    private String status;
    private Long tenantId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
