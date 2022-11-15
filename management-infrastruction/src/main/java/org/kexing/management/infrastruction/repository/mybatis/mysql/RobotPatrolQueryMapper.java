package org.kexing.management.infrastruction.repository.mybatis.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.domin.model.mysql.robot.RobotPatrol;
import org.kexing.management.infrastruction.query.dto.ListInspectRobotInspectionRequest;

@Mapper
public interface RobotPatrolQueryMapper extends BaseMapper<RobotPatrol> {
    IPage<RobotPatrol> selectListInspectPage(
            @Param("page") Page<?> page,
            @Param("listInspectRobotInspectionRequest") ListInspectRobotInspectionRequest listInspectRobotInspectionRequest);
}
