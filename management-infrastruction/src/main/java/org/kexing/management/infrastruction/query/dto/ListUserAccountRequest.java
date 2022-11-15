package org.kexing.management.infrastruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.kexing.management.infrastruction.query.BaseParam;

// TODO_LH: 2022/1/26 看spring的分页请求
/** @author lh */
@Data
public class ListUserAccountRequest extends BaseParam {

  private String organizationId;

  private Long staffId;

  @Schema(description = "账户状态")
  private Boolean enabled;
}
