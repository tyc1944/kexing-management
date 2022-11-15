package org.kexing.management.infrastruction.query.dto.sql_server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/** @author lh */
@Setter
@Getter
public class ListProductionOrderResponse {
  @Schema(description = "生产订单号")
  private int productOrderNo;

  @Schema(description = "参考编号")
  private String referenceNo;

  @Schema(description = "客户名称")
  private String customerName;

  @Schema(description = "计划生产日期")
  private Instant planProductTime;

  @Schema(description = "产品数量")
  private Integer productNum;

  @Schema(description = "bomId")
  private String bomId;
}
