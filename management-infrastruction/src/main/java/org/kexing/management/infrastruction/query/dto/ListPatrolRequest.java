package org.kexing.management.infrastruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.kexing.management.infrastruction.query.BaseParam;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Setter
@Getter
@Schema(description = "巡检记录列表")
public class ListPatrolRequest extends BaseParam {
    @Schema(description = "任务类型")
    @NotNull
    private PatrolRule.PatrolType patrolType;
}
