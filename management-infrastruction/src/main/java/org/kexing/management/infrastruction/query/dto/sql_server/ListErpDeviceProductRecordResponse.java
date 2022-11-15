package org.kexing.management.infrastruction.query.dto.sql_server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/** @author lh */
@Setter
@Getter
public class ListErpDeviceProductRecordResponse {
  @Schema(description = "生产订单号")
  private int productOrderNo;

  @Schema(description = "参考编号")
  private String referenceNo;

  @Schema(description = "订单产品标识")
  private Long orderProductId;

  @Schema(description = "客户名称")
  private String customerName;

  @Schema(description = "生产人员")
  private String producerName;

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
