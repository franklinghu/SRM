package com.srm.modules.supplier.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("supplier")
public class Supplier {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String supplierCode;
    private String supplierName;
    private String shortName;
    private Integer supplierType;
    private String industry;
    private String country;
    private String province;
    private String city;
    private String address;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private String website;
    private String taxNumber;
    private String bankName;
    private String bankAccount;
    private String creditLevel;
    private Integer status;
    private BigDecimal registerCapital;
    private Integer employeeCount;
    private String intro;
    private Long tenantId;
    private Long createdBy;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
