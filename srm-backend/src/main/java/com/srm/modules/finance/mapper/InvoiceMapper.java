package com.srm.modules.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.srm.modules.finance.entity.Invoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InvoiceMapper extends BaseMapper<Invoice> {
    IPage<Invoice> selectPageList(Page<?> page, @Param("invoiceNo") String invoiceNo,
                                   @Param("status") String status, @Param("tenantId") Long tenantId);
}
