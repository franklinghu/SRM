package com.srm.modules.evaluation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.srm.modules.evaluation.entity.EvaluationTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EvaluationTemplateMapper extends BaseMapper<EvaluationTemplate> {
    IPage<EvaluationTemplate> selectPageList(Page<?> page, @Param("templateName") String templateName,
                                              @Param("status") Integer status, @Param("tenantId") Long tenantId);
}
