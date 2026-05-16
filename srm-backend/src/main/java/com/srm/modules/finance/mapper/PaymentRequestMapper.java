package com.srm.modules.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.srm.modules.finance.entity.PaymentRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentRequestMapper extends BaseMapper<PaymentRequest> {
    IPage<PaymentRequest> selectPageList(Page<?> page, @Param("requestNo") String requestNo,
                                          @Param("status") String status, @Param("tenantId") Long tenantId);
}
