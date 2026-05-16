package com.srm.modules.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.srm.modules.contract.entity.Contract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ContractMapper extends BaseMapper<Contract> {
    IPage<Contract> selectPageList(Page<?> page, @Param("contractName") String contractName,
                                    @Param("status") String status, @Param("tenantId") Long tenantId);
}
