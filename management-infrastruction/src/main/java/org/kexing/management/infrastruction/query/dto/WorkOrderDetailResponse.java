package org.kexing.management.infrastruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/** @author lh */
@Setter
@Getter
public class WorkOrderDetailResponse extends ListWorkOrderResponse {
  @Schema(description = "设别名称")
  private String deviceName;

  @Schema(description = "设备位置")
  private String deviceLocation;

  @Schema(description = "指派账户名称")
  private String assignerStaffName;

  @Schema(description = "确认完成账户名称")
  private String confirmFinishStaffName;

  @Schema(description = "指派账户手机号")
  private String assignerPhone;
}
