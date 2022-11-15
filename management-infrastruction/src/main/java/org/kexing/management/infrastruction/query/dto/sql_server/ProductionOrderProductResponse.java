package org.kexing.management.infrastruction.query.dto.sql_server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;

/** @author lh */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProductionOrderProductResponse extends ListProductionOrderProductResponse {
  @Schema(description = "结束生产日期(页面生产日期使用该字段)")
  private Instant endProductionDateTime;

  @JsonIgnore private Integer endProductionTime;

  @Schema(description = "产品订单信息")
  private ProductionOrderResponse productionOrder;
}
