package com.srm.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("purchase_order")
public class PurchaseOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String orderNo;
    private Long quoteId;
    private Long supplierId;
    private String orderType;
    private BigDecimal orderAmount;
    private BigDecimal paidAmount;
    private String deliveryAddress;
    private LocalDate expectedDate;
    private LocalDate actualDate;
    private String paymentStatus;
    private String deliveryStatus;
    private String status;
    private Long tenantId;
    private Long createdBy;
    private Long approvedBy;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
