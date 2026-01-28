package com.crazydream.infrastructure.persistence.mapper;

import com.crazydream.infrastructure.persistence.po.SubGoalPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SubGoalPersistenceMapper {
    int insert(SubGoalPO subGoal);
    int update(SubGoalPO subGoal);
    SubGoalPO selectById(Long id);
    List<SubGoalPO> selectByGoalId(Long goalId);
    int deleteById(Long id);
    int batchDelete(@Param("ids") List<Long> ids);
}
