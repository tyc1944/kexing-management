package org.kexing.management.api_reset.resource;

import com.yunmo.domain.common.Events;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.kexing.management.infrastruction.query.dto.DeviceNameAndAttributesResponse;
import org.kexing.management.infrastruction.query.dto.RobotResponse;
import org.kexing.management.infrastruction.repository.mybatis.mysql.DeviceQueryMapper;
import org.kexing.management.infrastruction.util.PatrolRobotCommandUtil;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.kexing.management.infrastruction.util.PatrolRobotCommandUtil.createTaskStatusCommand;

/**
 * @author lh
 */
@RestController
@RequestMapping("/api/robots")
@RequiredArgsConstructor
@Tag(name = "机器人资源")
public class RobotResource {

  private final DeviceQueryMapper deviceQueryMapper;

  @PostMapping("/{id}/command")
  public void get(
      @PathVariable(value = "id") Long deviceId,
      @Valid @RequestBody RobotResource.TaskCommandRequest taskCommandRequest) {
    PatrolRobotCommandUtil.TaskCommand taskCommand =
        createTaskStatusCommand(taskCommandRequest.getTaskCommandEnum());
    Events.post(taskCommand);
  }

  @GetMapping("/{id}")
  public RobotResponse get(@PathVariable(value = "id") Long deviceId) { // todo_lh 需要电量信息
    DeviceNameAndAttributesResponse deviceNameAndAttributesResponse = deviceQueryMapper.selectByDeviceId(deviceId);
    return deviceNameAndAttributesResponse == null
        ? null
        : new RobotResponse(deviceNameAndAttributesResponse);
  }

  @Data
  public static class TaskCommandRequest {
    @NotNull private PatrolRobotCommandUtil.TaskCommandEnum taskCommandEnum;
  }
}
