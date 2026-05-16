package com.srm.modules.evaluation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.srm.modules.evaluation.entity.EvaluationTemplate;
import com.srm.modules.evaluation.entity.SupplierEvaluation;

public interface EvaluationService {
    IPage<EvaluationTemplate> getTemplatePage(Integer pageNum, Integer pageSize, String templateName, Integer status);
    EvaluationTemplate getTemplateById(Long id);
    EvaluationTemplate createTemplate(EvaluationTemplate template);
    EvaluationTemplate updateTemplate(EvaluationTemplate template);
    void deleteTemplate(Long id);

    IPage<SupplierEvaluation> getEvaluationPage(Integer pageNum, Integer pageSize, Long supplierId, String status);
    SupplierEvaluation getEvaluationById(Long id);
    SupplierEvaluation createEvaluation(SupplierEvaluation evaluation);
    SupplierEvaluation updateEvaluation(SupplierEvaluation evaluation);
    void deleteEvaluation(Long id);
}
