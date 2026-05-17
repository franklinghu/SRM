package com.srm.modules.evaluation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.srm.common.result.Result;
import com.srm.modules.evaluation.entity.EvaluationTemplate;
import com.srm.modules.evaluation.entity.SupplierEvaluation;
import com.srm.modules.evaluation.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @GetMapping("/templates")
    public Result<IPage<EvaluationTemplate>> getTemplatePage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String templateName,
            @RequestParam(required = false) Integer status) {
        IPage<EvaluationTemplate> page = evaluationService.getTemplatePage(pageNum, pageSize, templateName, status);
        return Result.success(page);
    }

    @GetMapping("/templates/{id}")
    public Result<EvaluationTemplate> getTemplateById(@PathVariable Long id) {
        EvaluationTemplate template = evaluationService.getTemplateById(id);
        return Result.success(template);
    }

    @PostMapping("/templates")
    public Result<EvaluationTemplate> createTemplate(@RequestBody EvaluationTemplate template) {
        EvaluationTemplate result = evaluationService.createTemplate(template);
        return Result.success(result);
    }

    @PutMapping("/templates/{id}")
    public Result<EvaluationTemplate> updateTemplate(@PathVariable Long id, @RequestBody EvaluationTemplate template) {
        template.setId(id);
        EvaluationTemplate result = evaluationService.updateTemplate(template);
        return Result.success(result);
    }

    @DeleteMapping("/templates/{id}")
    public Result<Void> deleteTemplate(@PathVariable Long id) {
        evaluationService.deleteTemplate(id);
        return Result.success();
    }

    @GetMapping("/evaluations")
    public Result<IPage<SupplierEvaluation>> getEvaluationPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) String status) {
        IPage<SupplierEvaluation> page = evaluationService.getEvaluationPage(pageNum, pageSize, supplierId, status);
        return Result.success(page);
    }

    @GetMapping("/evaluations/{id}")
    public Result<SupplierEvaluation> getEvaluationById(@PathVariable Long id) {
        SupplierEvaluation evaluation = evaluationService.getEvaluationById(id);
        return Result.success(evaluation);
    }

    @PostMapping("/evaluations")
    public Result<SupplierEvaluation> createEvaluation(@RequestBody SupplierEvaluation evaluation) {
        SupplierEvaluation result = evaluationService.createEvaluation(evaluation);
        return Result.success(result);
    }

    @PutMapping("/evaluations/{id}")
    public Result<SupplierEvaluation> updateEvaluation(@PathVariable Long id, @RequestBody SupplierEvaluation evaluation) {
        evaluation.setId(id);
        SupplierEvaluation result = evaluationService.updateEvaluation(evaluation);
        return Result.success(result);
    }

    @DeleteMapping("/evaluations/{id}")
    public Result<Void> deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
        return Result.success();
    }
}
