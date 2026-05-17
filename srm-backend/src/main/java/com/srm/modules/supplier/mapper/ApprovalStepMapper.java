package com.srm.modules.supplier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.srm.modules.supplier.entity.ApprovalStep;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ApprovalStepMapper extends BaseMapper<ApprovalStep> {
    @Select("SELECT * FROM approval_step WHERE flow_id = #{flowId} ORDER BY step_no ASC")
    List<ApprovalStep> selectByFlowId(@Param("flowId") Long flowId);
}
