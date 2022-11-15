package org.kexing.management.infrastruction.query.dto.sql_server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/** @author lh */
@Setter
@Getter
public class GetErpDeviceResponse extends ListErpDeviceResponse {
  @Schema(description = "生产订单号")
  private Integer productOrderNo;

  @Schema(description = "生产人员")
  private String producerName;

  @Schema(description = "产品名称")
  private String productName;

  @Schema(description = "产品型号")
  private String productModel;

  @Schema(description = "物料编号")
  private String materialId;

  @Schema(description = "产品编号(批次管制卡)")
  private String productNumber;

  @Schema(description = "工序名称")
  private String processName;

  @Schema(description = "开工时间")
  private Instant startProductionDateTime;

  @JsonIgnore private Integer startProductionTime;
  @Schema(description = "工艺编号")
  @JsonIgnore private String prcId;

  private ProductionOrderResponse ProductionOrderResponse;
}
