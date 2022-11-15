package org.kexing.management.infrastruction.query.dto.sql_server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.kexing.management.domin.util.ErpDeviceUtil;
import org.kexing.management.infrastruction.query.BaseParam;

import javax.validation.constraints.NotNull;

/** @author lh */
@Setter
@Getter
@Schema(description = "erp 设备列表请求参数")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ListErpDeviceRequest extends BaseParam {
  @Schema(description = "设备类型")
  @NotNull
  ErpDeviceUtil.ErpDeviceType erpDeviceType;
}
