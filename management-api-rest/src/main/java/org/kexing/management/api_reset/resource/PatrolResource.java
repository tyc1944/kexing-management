package org.kexing.management.api_reset.resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.kexing.management.api_reset.dto.RobotPatrolDetailResponse;
import org.kexing.management.domin.model.mysql.DeviceSource;
import org.kexing.management.domin.model.mysql.robot.RobotPatrol;
import org.kexing.management.domin.repository.mysql.RobotPatrolLineRepository;
import org.kexing.management.infrastruction.mapstruct.PageParamMapper;
import org.kexing.management.infrastruction.query.BaseParam;
import org.kexing.management.infrastruction.query.dto.ListInspectRobotInspectionRequest;
import org.kexing.management.infrastruction.repository.mybatis.mysql.RobotPatrolQueryMapper;
import org.kexing.management.infrastruction.service.DeviceViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

/**
 * @author lh
 */
@RestController
@RequestMapping("/api/patrols")
@RequiredArgsConstructor
@Tag(name = "巡检资源")
public class PatrolResource {
  @Autowired DeviceViewService deviceViewService;

  @Autowired RobotPatrolQueryMapper robotPatrolQueryMapper;

  @Autowired PageParamMapper pageParamMapper;

  @Autowired RobotPatrolLineRepository robotPatrolLineRepository;

  @GetMapping
  public IPage<RobotPatrol> get(
      @Validated(value = {BaseParam.BaseParamVaGroup.class, Default.class}) @ModelAttribute
          ListInspectRobotInspectionRequest listInspectRobotInspectionRequest) {
    return robotPatrolQueryMapper.selectListInspectPage(
        pageParamMapper.mapper(listInspectRobotInspectionRequest),
        listInspectRobotInspectionRequest);
  }

  @GetMapping("/{id}")
  public RobotPatrolDetailResponse get(@PathVariable(value = "id") RobotPatrol robotPatrol) {
    String deviceName =
        deviceViewService.getDeviceName(String.valueOf(robotPatrol.getRobotId()), DeviceSource.IOT);
    return RobotPatrolDetailResponse.builder()
        .robotPatrol(robotPatrol)
        .deviceName(deviceName)
        .build();
  }
}
