package com.srm.modules.supplier.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("supplier_application")
public class SupplierApplication {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String applyNo;
    private String supplierName;
    private String shortName;
    private Integer supplierType;
    private String industry;
    private String country;
    private String province;
    private String city;
    private String address;
    private String contactName;
    private String contactPosition;
    private String contactPhone;
    private String contactEmail;
    private String region;
    private String businessLicense;
    private String registeredCapital;
    private String mainProducts;
    private String annualRevenue;
    private String description;
    private String website;
    private String taxNumber;
    private String bankName;
    private String bankAccount;
    private BigDecimal registerCapital;
    private Integer employeeCount;
    private String intro;
    
    private String businessLicenseUrl;
    private String financialReportUrl;
    private String otherDocumentsUrl;
    
    private String username;
    private String password;
    
    private String status;
    private String rejectReason;
    
    private Long currentApproverId;
    private Integer currentStep;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
