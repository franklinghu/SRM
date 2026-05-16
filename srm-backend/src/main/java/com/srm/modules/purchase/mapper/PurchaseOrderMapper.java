package com.srm.modules.purchase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.srm.modules.purchase.entity.PurchaseOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {
    IPage<PurchaseOrder> selectPageList(Page<?> page, @Param("orderNo") String orderNo, 
                                        @Param("status") String status, @Param("tenantId") Long tenantId);
}
