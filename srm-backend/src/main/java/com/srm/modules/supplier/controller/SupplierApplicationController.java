package com.srm.modules.supplier.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.srm.common.result.Result;
import com.srm.modules.supplier.dto.SupplierApplicationDTO;
import com.srm.modules.supplier.entity.ApprovalRecord;
import com.srm.modules.supplier.entity.ApprovalStep;
import com.srm.modules.supplier.entity.SupplierApplication;
import com.srm.modules.supplier.service.SupplierApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SupplierApplicationController {

    private final SupplierApplicationService applicationService;

    @PostMapping("/supplier-applications")
    public Result<SupplierApplication> createApplication(@RequestBody SupplierApplicationDTO dto) {
        SupplierApplication application = applicationService.createApplication(dto);
        return Result.success(application);
    }

    @GetMapping("/supplier-applications/public")
    public Result<SupplierApplication> createPublicApplication(@RequestBody SupplierApplicationDTO dto) {
        SupplierApplication application = applicationService.createApplication(dto);
        return Result.success(application);
    }

    @GetMapping("/supplier-applications/{id}")
    public Result<SupplierApplication> getApplication(@PathVariable Long id) {
        SupplierApplication application = applicationService.getApplicationById(id);
        return Result.success(application);
    }

    @GetMapping("/supplier-applications")
    public Result<IPage<SupplierApplication>> getApplicationPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String status) {
        IPage<SupplierApplication> page = applicationService.getApplicationPage(pageNum, pageSize, status);
        return Result.success(page);
    }

    @GetMapping("/supplier-applications/{id}/records")
    public Result<List<ApprovalRecord>> getApprovalRecords(@PathVariable Long id) {
        List<ApprovalRecord> records = applicationService.getApprovalRecords(id);
        return Result.success(records);
    }

    @PostMapping("/supplier-applications/{id}/approve")
    public Result<SupplierApplication> approve(
            @PathVariable Long id,
            @RequestParam Long approverId,
            @RequestParam String action,
            @RequestParam(required = false) String comment) {
        SupplierApplication application = applicationService.approve(id, approverId, action, comment);
        return Result.success(application);
    }

    @GetMapping("/supplier-applications/expiring-certificates")
    public Result<List<Map<String, Object>>> getExpiringCertificates(
            @RequestParam(defaultValue = "30") Integer days) {
        List<Map<String, Object>> certificates = applicationService.getExpiringCertificates(days);
        return Result.success(certificates);
    }

    @GetMapping("/approval-flow/steps")
    public Result<List<ApprovalStep>> getApprovalSteps() {
        return Result.success(applicationService.getApprovalSteps(1L));
    }
}
