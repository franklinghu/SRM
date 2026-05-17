package com.srm.modules.supplier.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SupplierApplicationDTO {
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
    private BigDecimal registerCapital;
    private Integer employeeCount;
    private String intro;
    private String businessLicenseUrl;
    private String financialReportUrl;
    private String otherDocumentsUrl;
}
