package org.kexing.management.infrastruction.repository.mybatis.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.kexing.management.domin.model.mysql.robot.RobotPatrolLine;
import org.kexing.management.infrastruction.mapstruct.PageParamMapper;
import org.kexing.management.infrastruction.query.dto.ListPatrolRequest;

@Mapper
public interface PatrolRuleQueryMapper extends BaseMapper<PatrolRule> {
    IPage<PatrolRule> selectListPatrolPage(
            @Param("page") Page<?> page,
            @Param("listPatrolRequest") ListPatrolRequest listPatrolRequest);
}
