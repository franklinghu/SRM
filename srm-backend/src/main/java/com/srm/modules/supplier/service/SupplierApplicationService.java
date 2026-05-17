package com.srm.modules.supplier.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.srm.modules.supplier.dto.SupplierApplicationDTO;
import com.srm.modules.supplier.entity.ApprovalFlow;
import com.srm.modules.supplier.entity.ApprovalRecord;
import com.srm.modules.supplier.entity.ApprovalStep;
import com.srm.modules.supplier.entity.SupplierApplication;

import java.util.List;
import java.util.Map;

public interface SupplierApplicationService {
    SupplierApplication createApplication(SupplierApplicationDTO dto);
    
    SupplierApplication getApplicationById(Long id);
    
    IPage<SupplierApplication> getApplicationPage(Integer pageNum, Integer pageSize, String status);
    
    List<ApprovalRecord> getApprovalRecords(Long applicationId);
    
    SupplierApplication approve(Long applicationId, Long approverId, String action, String comment);
    
    ApprovalFlow getApprovalFlow(String flowCode);
    
    List<ApprovalStep> getApprovalSteps(Long flowId);
    
    List<Map<String, Object>> getExpiringCertificates(Integer days);
    
    void createDefaultApprovalFlow();
}
