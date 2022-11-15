package org.kexing.management.infrastruction.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 巡检机器人命令.
 *
 * @author lh
 */
public interface PatrolRobotCommandUtil {

   static TaskCommand createTaskStatusCommand(TaskCommandEnum takTaskCommandEnum) {
    String task_command;
    switch (takTaskCommandEnum) {
      case RESUME_PATROL:
        task_command = "1";
        break;
      case PAUSE_PATROL:
        task_command = "2";
        break;
      case END_PATROL:
        task_command = "3";
        break;
      default:
        throw new UnsupportedOperationException();
    }
    return TaskCommand.builder().type("234").task_command(task_command).build();
  }

  static TaskCommand createTaskCommand(String taskId){
    return TaskCommand.builder().type("233").task_id(taskId).task_command("1").build();
  }

  @Schema(description = "RESUME_PATROL->恢复巡逻,PAUSE_PATROL->停止,END_PATROL->结束巡逻")
  enum TaskCommandEnum {
    RESUME_PATROL,
    PAUSE_PATROL,
    END_PATROL,
  }

  @Data
  @SuperBuilder
  @NoArgsConstructor
  @AllArgsConstructor
  abstract class PatrolRobotCommand {
    /** 任务指令 */
    private String type;
  }

  @Data
  @SuperBuilder
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  class TaskCommand extends PatrolRobotCommand {
    /** 任务具体操作指令 */
    private String task_command;
    /** 任务编号` */
    private String task_id;
  }

}
