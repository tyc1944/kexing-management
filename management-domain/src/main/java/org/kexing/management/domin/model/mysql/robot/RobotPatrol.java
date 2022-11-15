package org.kexing.management.domin.model.mysql.robot;

import com.yunmo.domain.common.Audited;
import com.yunmo.generator.annotation.AutoValueDTO;
import com.yunmo.generator.annotation.ValueField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.EnumType.STRING;

@Entity
@Data
@Builder
@AutoValueDTO
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "机器人巡检")
@Table
public class RobotPatrol extends Audited {
  @Id
  @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
  @GeneratedValue(generator = "sequence_id")
  private Long id;

  private Long robotId;
  private Instant startTime;
  @ValueField private String patrolNum;
  private Instant endTime;

  @Column(length = 500)
  private String patrolVideo;

  @ValueField
  @Enumerated(value = STRING)
  private PatrolRule.PatrolType patrolType;

  @ValueField
  @Enumerated(value = STRING)
  private TaskStatus taskStatus;

  @Type(type = "json")
  @Column(columnDefinition = "json")
  @Schema(description = "巡查的路线")
  private RobotPatrolLine robotPatrolLine;

  @Schema(description = "是否关联运行规则")
  private boolean associatedPatrolRule;

  @Schema(description = "任务状态:NO_RUNNING->未运行,PATROLLING->巡逻中,PAUSE_PATROL-> 暂停巡逻,FORCED_END->强制结束,END->结束")
  public enum TaskStatus {
    NO_RUNNING,
    PATROLLING,
    PAUSE_PATROL,
    FORCED_END,
    END
  }
}
