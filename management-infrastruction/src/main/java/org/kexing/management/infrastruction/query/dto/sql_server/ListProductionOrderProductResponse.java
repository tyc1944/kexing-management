package org.kexing.management.infrastruction.query.dto.sql_server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/** @author lh */
@Setter
@Getter
public class ListProductionOrderProductResponse {
  @Schema(description = "订单产品标识")
  private Long orderProductId;

  @Schema(description = "产品名称")
  private String productName;

  @Schema(description = "产品型号")
  private String productModel;

  @Schema(description = "产品编号")
  private String productNumber;

  @Schema(description = "开始生产日期(页面生产日期使用该字段)")
  private Instant startProductionDateTime;

  @JsonIgnore private Integer startProductionTime;
}
