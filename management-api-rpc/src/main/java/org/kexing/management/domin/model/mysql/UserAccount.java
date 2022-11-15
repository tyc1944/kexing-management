package org.kexing.management.domin.model.mysql;

import com.yunmo.attendance.api.entity.Organization;
import com.yunmo.attendance.api.entity.Staff;
import com.yunmo.domain.common.Audited;
import com.yunmo.generator.annotation.AutoValueDTO;
import com.yunmo.generator.annotation.ValueField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

/** @author lh */
@Entity
@Getter
@Setter
@Builder
@AutoValueDTO
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@org.hibernate.annotations.Table(appliesTo = "user_account", comment = "账户")
@Table(
    indexes = {
      @Index(name = "UK_ACCOUNT_NAME_INDEX", columnList = "accountName"),
      @Index(name = "EMPLOYEE_ID_INDEX", columnList = "staffId")
    })
@SQLDelete(sql = "update user_account set deleted = 1 where id = ?")
@Where(clause = "deleted = 0")
public class UserAccount extends Audited {

  @Id
  @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
  @GeneratedValue(generator = "sequence_id")
  private Long id;

  @Column(nullable = false)
  @ValueField
  private String accountName;

  @Column(nullable = false)
  @ValueField(value = "ResetPassword")
  private String password;

  @Embedded private WechatLogin wechatLogin;

  /** 员工id */
  @Column(nullable = false)
  @ValueField
  @NotNull
  private Long staffId;

  private String phone;

  /** 账户启用 */
  @Column(nullable = false)
  @Builder.Default
  @ValueField(value = "enabled",noDefault = true)
  private boolean enabled = true;

  @Schema(description = "允许人员闯入")
  @Column(nullable = false)
  @ValueField
  private boolean peopleBrokeIntoAllow;

  /** 权限配置 */
  @Column(columnDefinition = "text")
  @ValueField private String permissionConf;

  @Schema(description = "修改密码时间")
  private Instant changePasswordTime;

  @Schema(description = "修改权限时间")
  private Instant changePermissionConfTime;

  private boolean deleted;

  @Transient private Staff staff;

  @Transient private Organization organization;

  @Setter
  @Getter
  @Embeddable
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class WechatLogin {
    /** 账户唯一标识 */
    private String openId;
    /** 会话密钥 */
    private String sessionKey;

    /** 会话密钥 */
    private String unionId;
  }

  public void changePassword(PasswordEncoder passwordEncoder, String newPassword) {
    password = passwordEncoder.encode(newPassword);
    changePasswordTime = Instant.now();
  }

  public void changePermissionConf(String newPermissionConf) {
    if (!StringUtils.equals(permissionConf, newPermissionConf)) {
      changePermissionConfTime = Instant.now();
    }
    permissionConf = newPermissionConf;
  }
}
