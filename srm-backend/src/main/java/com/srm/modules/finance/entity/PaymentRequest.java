package com.srm.modules.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("payment_request")
public class PaymentRequest {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String requestNo;
    private Long supplierId;
    private Long invoiceId;
    private Long orderId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String status;
    private String remarks;
    private Long tenantId;
    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
