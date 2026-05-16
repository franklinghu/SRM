package com.srm.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("contract")
public class Contract {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String contractNo;
    private String contractName;
    private Long supplierId;
    private String contractType;
    private BigDecimal totalAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String signDate;
    private String partyA;
    private String partyB;
    private String content;
    private String attachmentUrl;
    private String status;
    private Long tenantId;
    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
