package org.kexing.management.infrastruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.kexing.management.infrastruction.query.BaseParam;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Setter
@Getter
@Schema(description = "门禁出入记录查询参数")
public class ListFaceDoorRecordRequest extends BaseParam {
    private Long deviceId;
    @Schema(description = "查询时间范围::[开始,结束]")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Size(min = 2, max = 2)
    private LocalDate[] dateRange;
    private String organizationId;
    private Long staffId;
    private Boolean abnormalTemperature;
}
