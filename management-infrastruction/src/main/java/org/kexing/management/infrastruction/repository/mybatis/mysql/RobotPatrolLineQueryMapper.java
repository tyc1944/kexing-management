package org.kexing.management.infrastruction.repository.mybatis.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.kexing.management.domin.model.mysql.robot.RobotPatrolLine;

import java.util.List;

@Mapper
public interface RobotPatrolLineQueryMapper extends BaseMapper<RobotPatrolLine> {
    List<RobotPatrolLine> findByRobotId(@Param("robotId") Long robotId, @Param("patrolType")String patrolType);

  RobotPatrolLine findById(@Param("id") Long id);
}
