package org.kexing.management.api_reset.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/** @author lh */
@Data
public class ChangePasswordRequest {
  @NotNull(message = "新密码不能为空")
  private String newPassword;

  @NotNull(message = "旧密码不能为空")
  private String oldPassword;

  public boolean newOldPasswordIsSame() {
    return oldPassword.equals(newPassword);
  }
}
