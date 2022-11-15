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
@Schema(description = "生产订单产品工艺列表查询请求参数")
public class ListProductionOrderProductCraftRequest extends BaseParam {
  @Schema(description = "生产订单产品工艺时间范围::[开始,结束]")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Size(min = 2, max = 2)
  private LocalDate[] dateRange;
}
