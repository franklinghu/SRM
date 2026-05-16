package com.srm.modules.purchase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.srm.modules.purchase.entity.PurchaseRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PurchaseRequestMapper extends BaseMapper<PurchaseRequest> {
    IPage<PurchaseRequest> selectPageList(Page<?> page, @Param("title") String title, 
                                          @Param("status") String status, @Param("tenantId") Long tenantId);
}
