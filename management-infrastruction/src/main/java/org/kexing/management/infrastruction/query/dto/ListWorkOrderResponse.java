package org.kexing.management.infrastruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.kexing.management.domin.model.mysql.WorkOrder;

/** @author lh */
@Setter
@Getter
public class ListWorkOrderResponse {
  private WorkOrder workOrder;

  @Schema(description = "处理人名称")
  private String processorStaffName;
}
