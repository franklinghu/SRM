package com.srm.modules.evaluation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.srm.modules.evaluation.entity.SupplierEvaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SupplierEvaluationMapper extends BaseMapper<SupplierEvaluation> {
    IPage<SupplierEvaluation> selectPageList(Page<?> page, @Param("supplierId") Long supplierId,
                                              @Param("status") String status, @Param("tenantId") Long tenantId);
}
