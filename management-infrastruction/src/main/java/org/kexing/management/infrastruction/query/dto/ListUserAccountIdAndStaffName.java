package org.kexing.management.infrastruction.query.dto;

import com.yunmo.generator.annotation.ValueField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/** @author lh */
@Setter
@Getter
public class ListUserAccountIdAndStaffName {
  @Schema(description = "账户id")
  private Long userAccountId;

  @Schema(description = "账户对应的员工列表")
  private String userAccountStaffName;

  /** 账户启用 */
  @Schema(description = "账户是否启用")
  private Boolean enabled;
}
