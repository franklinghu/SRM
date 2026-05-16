package com.srm.modules.evaluation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.srm.modules.evaluation.entity.EvaluationTemplate;
import com.srm.modules.evaluation.entity.SupplierEvaluation;
import com.srm.modules.evaluation.mapper.EvaluationTemplateMapper;
import com.srm.modules.evaluation.mapper.SupplierEvaluationMapper;
import com.srm.modules.evaluation.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationTemplateMapper templateMapper;
    private final SupplierEvaluationMapper evaluationMapper;

    @Override
    public IPage<EvaluationTemplate> getTemplatePage(Integer pageNum, Integer pageSize, String templateName, Integer status) {
        Page<EvaluationTemplate> page = new Page<>(pageNum, pageSize);
        return templateMapper.selectPageList(page, templateName, status, 1L);
    }

    @Override
    public EvaluationTemplate getTemplateById(Long id) {
        return templateMapper.selectById(id);
    }

    @Override
    @Transactional
    public EvaluationTemplate createTemplate(EvaluationTemplate template) {
        template.setTenantId(1L);
        templateMapper.insert(template);
        return template;
    }

    @Override
    @Transactional
    public EvaluationTemplate updateTemplate(EvaluationTemplate template) {
        templateMapper.updateById(template);
        return template;
    }

    @Override
    @Transactional
    public void deleteTemplate(Long id) {
        templateMapper.deleteById(id);
    }

    @Override
    public IPage<SupplierEvaluation> getEvaluationPage(Integer pageNum, Integer pageSize, Long supplierId, String status) {
        Page<SupplierEvaluation> page = new Page<>(pageNum, pageSize);
        return evaluationMapper.selectPageList(page, supplierId, status, 1L);
    }

    @Override
    public SupplierEvaluation getEvaluationById(Long id) {
        return evaluationMapper.selectById(id);
    }

    @Override
    @Transactional
    public SupplierEvaluation createEvaluation(SupplierEvaluation evaluation) {
        evaluation.setTenantId(1L);
        evaluationMapper.insert(evaluation);
        return evaluation;
    }

    @Override
    @Transactional
    public SupplierEvaluation updateEvaluation(SupplierEvaluation evaluation) {
        evaluationMapper.updateById(evaluation);
        return evaluation;
    }

    @Override
    @Transactional
    public void deleteEvaluation(Long id) {
        evaluationMapper.deleteById(id);
    }
}
