package com.srm.modules.supplier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.srm.modules.supplier.entity.ApprovalRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ApprovalRecordMapper extends BaseMapper<ApprovalRecord> {
    @Select("SELECT * FROM approval_record WHERE application_id = #{applicationId} ORDER BY created_at DESC")
    List<ApprovalRecord> selectByApplicationId(@Param("applicationId") Long applicationId);
}
