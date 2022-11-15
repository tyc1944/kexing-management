package org.kexing.management.domin.model.mysql.robot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yunmo.domain.common.Audited;
import com.yunmo.generator.annotation.AutoValueDTO;
import com.yunmo.generator.annotation.ValueField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
@Setter
@Getter
@Builder
@AutoValueDTO
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table
@Schema(description = "巡检规则")
public class PatrolRule extends Audited {

    private static final String CREATE = "Create";

    @Id
    @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
    @GeneratedValue(generator = "sequence_id")
    private Long id;

    @Schema(description = "任务编号")
    @Column(columnDefinition = "varchar(200)")
    @ValueField
    private String taskId;

    @Schema(description = "任务名称")
    @Column(columnDefinition = "varchar(200)")
    @ValueField(value = {CREATE})
    private String taskName;

    @Valid
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "robotPatrolLineId") //todo_lh 测试字段是否能识别
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ValueField(value = {CREATE})
    private RobotPatrolLine robotPatrolLine;

    @Schema(description = "巡检日期")
    @Type(type = "json")
    @Column(columnDefinition = "json")
    @ValueField(value = {CREATE})
    @Valid
    private List<TaskStartTime> taskStartTime;

  @Setter
  @Getter
  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TaskStartTime implements Serializable {
    @Schema(description = "每周几")
    @NotNull
    private DayOfWeek weekDay;

    @Schema(description = "每天时间")
    @NotNull
    private LocalTime everyDayTime;

  }

    @Schema(description = "任务类型: temporary  临时 daily  日常")
    @NotNull
    @ValueField(value = {CREATE})
    @Column(columnDefinition = "varchar(200) comment 'temporary  临时 daily  日常'")
    @Enumerated(value = EnumType.STRING)
    private PatrolType patrolType;

    @Schema(description = "巡检类型: REPEAT_INSPECTION->重复巡检，ONCE_INSPECTION-> 一次巡检")
    public enum PatrolType{
        REPEAT_INSPECTION,
        ONCE_INSPECTION
    }

    public static boolean existRepeatedStartTime(List<TaskStartTime> taskStartTimes) {
        return taskStartTimes.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .anyMatch(m -> m.getValue() > 1);
    }

}
