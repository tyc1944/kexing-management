package org.kexing.management.infrastruction.query.dto.sql_server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.kexing.management.domin.model.mysql.ErpDeviceConfig;

import java.util.List;

/**
 * @author lh
 */
@Setter
@Getter
@Schema(description = "烘箱信息")
public class GetErpHongXiangDeviceResponse extends GetErpDeviceResponse{

    @Schema(description = "当前烘箱温度")
    private Double currentTemperature;

  private ErpDeviceConfig.HongXiangDeviceConfig hongXiangDeviceConfig;

  private List<GetErpDeviceResponse> hongXiangGroup;
}
