package org.kexing.management.infrastruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author lh
 */
@Data
@Schema(description = "决定是否删除的的资源")
@Builder
public class ResourceDecideWhetherToDeleteUserAccountResponse {
  private boolean haveWorkOrder;
}
