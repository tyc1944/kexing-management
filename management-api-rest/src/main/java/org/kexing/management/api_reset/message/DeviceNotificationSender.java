package org.kexing.management.api_reset.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunmo.domain.common.Events;
import com.yunmo.domain.common.TopicMessage;
import com.yunmo.iot.pipe.core.Topics;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.kexing.management.domin.util.RobotPatrolUtil;
import org.kexing.management.infrastruction.util.PatrolRobotCommandUtil;
import org.kexing.management.infrastruction.util.PatrolRobotConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author lh
 */
@Component
public class DeviceNotificationSender {
  @Autowired ObjectMapper mapper;

  @EventListener
  @SneakyThrows
  public void sendConfig(PatrolRobotConfigUtil.RobotRegularPatrolConfig robotRegularPatrolConfig) {
    Topics.Topic topic = Topics.deviceConfig(RobotPatrolUtil.DEFAULT_PATROL_ROBOT_ID);
    byte[] content =
        mapper.writeValueAsString(robotRegularPatrolConfig).getBytes(StandardCharsets.UTF_8);
    Events.post(new TopicMessage().setTopic(topic.toString()).setRetain(true).setContent(content));
  }

  @EventListener
  @SneakyThrows
  public void sendCommand(PatrolRobotCommandUtil.TaskCommand taskCommand) {
    byte[] content = mapper.writeValueAsString(taskCommand).getBytes(StandardCharsets.UTF_8);
    Topics.Topic topic = Topics.deviceCommand(RobotPatrolUtil.DEFAULT_PATROL_ROBOT_ID, null);
    Events.post(new TopicMessage().setTopic(topic.toString()).setContent(content));
  }
}
