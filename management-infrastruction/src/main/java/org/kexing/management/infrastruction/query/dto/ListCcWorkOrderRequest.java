package org.kexing.management.infrastruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.kexing.management.domin.model.mysql.WorkOrder;
import org.kexing.management.infrastruction.query.BaseParam;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;

/** @author lh */
@Setter
@Getter
@Schema(description = "工单列表查询参数")
public class ListCcWorkOrderRequest extends BaseParam {

  public WorkOrder.Type type;

  @Schema(description = "工单查询时间范围::[开始,结束]")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Size(min = 2, max = 2)
  private LocalDate[] dateRange;

  public WorkOrder.Status status;

  @Schema(description = "抄送工单已查看")
  public Boolean haveRead;
}
