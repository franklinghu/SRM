package com.srm.modules.supplier.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("supplier_contact")
public class SupplierContact {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long supplierId;
    private String contactName;
    private String department;
    private String position;
    private String phone;
    private String mobile;
    private String email;
    private Integer isPrimary;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
