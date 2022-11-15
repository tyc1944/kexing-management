package org.kexing.management.infrastruction.query.dto.sql_server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lh
 */
@Setter
@Getter
@Schema(description = "包含物料信息,工作台和绕线机使用")
public class GetErpHRDeviceResponse extends GetErpDeviceResponse{
    @Schema(description = "工序物料清单")
    private List<MeterialResponse> craftMaterials;
}
