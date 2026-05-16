package com.srm.modules.supplier.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("supplier_certificate")
public class SupplierCertificate {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long supplierId;
    private String certType;
    private String certName;
    private String certNumber;
    private LocalDate issueDate;
    private LocalDate expireDate;
    private String fileUrl;
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
