package org.kexing.management.infrastruction.query.dto.sql_server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/** @author lh */
@Setter
@Getter
public class ListProductionOrderProductCraftResponse {
  @Schema(description = "订单产品标识")
  private Long orderProductId;

  @Schema(description = "工序名称")
  private String processName;

  @Schema(description = "工序投料人员名称(可作为生产人员使用)")
  private String producerName;

  @Schema(description = "开始生产时间")
  private Instant startProductionDateTime;

  @JsonIgnore private Integer startProductionTime;

  @Schema(description = "结束生产时间")
  private Instant endProductionDateTime;

  @Schema(description = "工序的所在顺序")
  private Integer lineNum;

  @Schema(description = "设备id")
  private String deviceId;

  @JsonIgnore private Integer endProductionTime;
}
