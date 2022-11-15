package org.kexing.management.infrastruction.query.dto.sql_server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.kexing.management.infrastruction.query.BaseParam;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;

/** @author lh */
@Setter
@Getter
public class ListErpDeviceProductRecordRequest extends BaseParam {
  @Schema(description = "生产时间范围::[开始,结束]")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Size(min = 2, max = 2)
  private LocalDate[] dateRange;
}
