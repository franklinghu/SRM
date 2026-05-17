package com.srm.modules.supplier.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.srm.modules.supplier.dto.SupplierApplicationDTO;
import com.srm.modules.supplier.entity.*;
import com.srm.modules.supplier.mapper.*;
import com.srm.modules.supplier.service.SupplierApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierApplicationServiceImpl implements SupplierApplicationService {

    private final SupplierApplicationMapper applicationMapper;
    private final ApprovalFlowMapper flowMapper;
    private final ApprovalStepMapper stepMapper;
    private final ApprovalRecordMapper recordMapper;
    private final SupplierMapper supplierMapper;
    private final SupplierCertificateMapper certificateMapper;

    @Override
    @Transactional
    public SupplierApplication createApplication(SupplierApplicationDTO dto) {
        SupplierApplication application = new SupplierApplication();
        application.setApplyNo("SA" + System.currentTimeMillis());
        application.setSupplierName(dto.getSupplierName());
        application.setShortName(dto.getShortName());
        application.setSupplierType(dto.getSupplierType());
        application.setIndustry(dto.getIndustry());
        application.setCountry(dto.getCountry());
        application.setProvince(dto.getProvince());
        application.setCity(dto.getCity());
        application.setAddress(dto.getAddress());
        application.setContactName(dto.getContactName());
        application.setContactPhone(dto.getContactPhone());
        application.setContactEmail(dto.getContactEmail());
        application.setWebsite(dto.getWebsite());
        application.setTaxNumber(dto.getTaxNumber());
        application.setBankName(dto.getBankName());
        application.setBankAccount(dto.getBankAccount());
        application.setRegisterCapital(dto.getRegisterCapital());
        application.setEmployeeCount(dto.getEmployeeCount());
        application.setIntro(dto.getIntro());
        application.setBusinessLicenseUrl(dto.getBusinessLicenseUrl());
        application.setFinancialReportUrl(dto.getFinancialReportUrl());
        application.setOtherDocumentsUrl(dto.getOtherDocumentsUrl());
        application.setStatus("pending");
        application.setCurrentStep(0);
        
        applicationMapper.insert(application);
        
        ApprovalFlow flow = getApprovalFlow("SUPPLIER_APPROVAL");
        if (flow != null) {
            List<ApprovalStep> steps = getApprovalSteps(flow.getId());
            if (!steps.isEmpty()) {
                ApprovalStep firstStep = steps.get(0);
                application.setCurrentApproverId(firstStep.getUserId());
                application.setCurrentStep(firstStep.getStepNo());
                applicationMapper.updateById(application);
            }
        }
        
        return application;
    }

    @Override
    public SupplierApplication getApplicationById(Long id) {
        return applicationMapper.selectById(id);
    }

    @Override
    public IPage<SupplierApplication> getApplicationPage(Integer pageNum, Integer pageSize, String status) {
        Page<SupplierApplication> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SupplierApplication> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(SupplierApplication::getStatus, status);
        }
        wrapper.orderByDesc(SupplierApplication::getCreatedAt);
        return applicationMapper.selectPage(page, wrapper);
    }

    @Override
    public List<ApprovalRecord> getApprovalRecords(Long applicationId) {
        return recordMapper.selectByApplicationId(applicationId);
    }

    @Override
    @Transactional
    public SupplierApplication approve(Long applicationId, Long approverId, String action, String comment) {
        SupplierApplication application = applicationMapper.selectById(applicationId);
        if (application == null) {
            throw new RuntimeException("申请不存在");
        }
        
        ApprovalFlow flow = getApprovalFlow("SUPPLIER_APPROVAL");
        List<ApprovalStep> steps = getApprovalSteps(flow.getId());
        
        ApprovalRecord record = new ApprovalRecord();
        record.setApplicationId(applicationId);
        record.setStepId(steps.get(application.getCurrentStep() - 1).getId());
        record.setStepNo(application.getCurrentStep());
        record.setStepName(steps.get(application.getCurrentStep() - 1).getStepName());
        record.setApproverId(approverId);
        record.setApproverName("用户" + approverId);
        record.setAction(action);
        record.setComment(comment);
        recordMapper.insert(record);
        
        if ("reject".equals(action)) {
            application.setStatus("rejected");
            application.setRejectReason(comment);
            applicationMapper.updateById(application);
            return application;
        }
        
        if (application.getCurrentStep() >= steps.size()) {
            application.setStatus("approved");
            createSupplierFromApplication(application);
        } else {
            application.setCurrentStep(application.getCurrentStep() + 1);
            if (application.getCurrentStep() <= steps.size()) {
                application.setCurrentApproverId(steps.get(application.getCurrentStep() - 1).getId());
            }
        }
        
        applicationMapper.updateById(application);
        return application;
    }

    private void createSupplierFromApplication(SupplierApplication application) {
        Supplier supplier = new Supplier();
        supplier.setSupplierCode("SUP" + String.format("%03d", application.getId()));
        supplier.setSupplierName(application.getSupplierName());
        supplier.setShortName(application.getShortName());
        supplier.setSupplierType(application.getSupplierType());
        supplier.setIndustry(application.getIndustry());
        supplier.setCountry(application.getCountry());
        supplier.setProvince(application.getProvince());
        supplier.setCity(application.getCity());
        supplier.setAddress(application.getAddress());
        supplier.setContactName(application.getContactName());
        supplier.setContactPhone(application.getContactPhone());
        supplier.setContactEmail(application.getContactEmail());
        supplier.setWebsite(application.getWebsite());
        supplier.setTaxNumber(application.getTaxNumber());
        supplier.setBankName(application.getBankName());
        supplier.setBankAccount(application.getBankAccount());
        supplier.setRegisterCapital(application.getRegisterCapital());
        supplier.setEmployeeCount(application.getEmployeeCount());
        supplier.setIntro(application.getIntro());
        supplier.setStatus(1);
        supplier.setTenantId(1L);
        supplierMapper.insert(supplier);
    }

    @Override
    public ApprovalFlow getApprovalFlow(String flowCode) {
        LambdaQueryWrapper<ApprovalFlow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApprovalFlow::getFlowCode, flowCode);
        return flowMapper.selectOne(wrapper);
    }

    @Override
    public List<ApprovalStep> getApprovalSteps(Long flowId) {
        return stepMapper.selectByFlowId(flowId);
    }

    @Override
    public List<Map<String, Object>> getExpiringCertificates(Integer days) {
        LocalDateTime expireDate = LocalDateTime.now().plusDays(days);
        List<SupplierCertificate> certificates = certificateMapper.selectList(
            new LambdaQueryWrapper<SupplierCertificate>()
                .le(SupplierCertificate::getExpireDate, expireDate.toLocalDate())
                .ge(SupplierCertificate::getExpireDate, LocalDateTime.now().toLocalDate())
        );
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (SupplierCertificate cert : certificates) {
            Map<String, Object> item = new HashMap<>();
            item.put("certificate", cert);
            item.put("daysRemaining", java.time.temporal.ChronoUnit.DAYS.between(
                LocalDateTime.now().toLocalDate(), cert.getExpireDate()));
            result.add(item);
        }
        return result;
    }

    @Override
    @Transactional
    public void createDefaultApprovalFlow() {
        LambdaQueryWrapper<ApprovalFlow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApprovalFlow::getFlowCode, "SUPPLIER_APPROVAL");
        if (flowMapper.selectOne(wrapper) != null) {
            return;
        }
        
        ApprovalFlow flow = new ApprovalFlow();
        flow.setFlowName("供应商准入审批流程");
        flow.setFlowCode("SUPPLIER_APPROVAL");
        flow.setModule("supplier");
        flow.setStatus(1);
        flowMapper.insert(flow);
        
        String[][] steps = {
            {"1", "采购员", "PURCHASER"},
            {"2", "采购经理", "PURCHASE_MANAGER"},
            {"3", "品质部", "QUALITY_DEPT"},
            {"4", "事业部", "BUSINESS_UNIT"},
            {"5", "财务部", "FINANCE_DEPT"},
            {"6", "总经办", "EXECUTIVE_OFFICE"}
        };
        
        for (String[] step : steps) {
            ApprovalStep approvalStep = new ApprovalStep();
            approvalStep.setFlowId(flow.getId());
            approvalStep.setStepNo(Integer.parseInt(step[0]));
            approvalStep.setStepName(step[1]);
            approvalStep.setRoleCode(step[2]);
            approvalStep.setUserId(1L);
            approvalStep.setIsRequired(1);
            approvalStep.setIsParallel(0);
            stepMapper.insert(approvalStep);
        }
    }
}
