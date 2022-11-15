package org.kexing.management.infrastruction.query.dto.sql_server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/** @author lh */
@Getter
@Setter
public class ProductionOrderResponse extends ListProductionOrderResponse {
  @Schema(description = "交货日期")
  private Instant deliveryDateTime;

  @JsonIgnore private Integer deliveryTime;
}
