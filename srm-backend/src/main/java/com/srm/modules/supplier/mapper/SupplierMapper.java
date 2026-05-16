package com.srm.modules.supplier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.srm.modules.supplier.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {
    IPage<Supplier> selectPageList(Page<?> page, @Param("supplierName") String supplierName, 
                                    @Param("status") Integer status, @Param("tenantId") Long tenantId);
}
