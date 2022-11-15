package org.kexing.management.infrastruction.query.dto.sql_server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.kexing.management.domin.model.mysql.ErpDeviceConfig;

import java.math.BigDecimal;

/** @author lh */
@Setter
@Getter
@Schema(description = "烘箱设备信息")
public class ListErpHongXiangDeviceResponse extends ListErpDeviceResponse {
  @Schema(description = "当前烘箱温度")
  private Double currentTemperature;

  @Schema(description = "烘箱配置参数")
  private ErpDeviceConfig.HongXiangDeviceConfig hongXiangDeviceConfig;
}
