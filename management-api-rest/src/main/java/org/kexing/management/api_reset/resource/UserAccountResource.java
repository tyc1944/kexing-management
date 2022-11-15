package org.kexing.management.api_reset.resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yunmo.domain.common.Principal;
import com.yunmo.domain.common.Tenant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.kexing.management.api_reset.dto.ChangePasswordRequest;
import org.kexing.management.domin.model.mysql.UserAccount;
import org.kexing.management.domin.model.mysql.value.UserAccountResetPassword;
import org.kexing.management.domin.model.mysql.value.UserAccountValue;
import org.kexing.management.domin.model.mysql.value.UserAccountenabled;
import org.kexing.management.domin.repository.mysql.UserAccountRepository;
import org.kexing.management.domin.repository.mysql.WorkOrderRepository;
import org.kexing.management.domin.service.UserAccountService;
import org.kexing.management.infrastruction.query.BaseParam;
import org.kexing.management.infrastruction.query.dto.ListUserAccountIdAndStaffName;
import org.kexing.management.infrastruction.query.dto.ListUserAccountRequest;
import org.kexing.management.infrastruction.query.dto.ResourceDecideWhetherToDeleteUserAccountResponse;
import org.kexing.management.infrastruction.service.UserAccountViewService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static org.kexing.management.domin.model.mysql.WorkOrder.Status.*;

/** @author lh */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Transactional
@Tag(name = "账户资源")
public class UserAccountResource {
  private final WorkOrderRepository workOrderRepository;
  private final UserAccountRepository userAccountRepository;

  private final UserAccountViewService userAccountViewService;

  private final PasswordEncoder passwordEncoder;

  private final UserAccountService userAccountService;

  @PostMapping
  @Operation(description = "创建账户")
  public UserAccount createUserAccount(@Validated @RequestBody UserAccountValue userAccountValue) {
    userAccountValue.setPassword(passwordEncoder.encode(userAccountValue.getPassword()));
    return userAccountService.createUserAccount(userAccountValue);
  }

  @GetMapping("/{userAccountId}")
  @Operation(description = "账户详情")
  public UserAccount getUserAccount(@Principal Tenant tenant, @PathVariable long userAccountId) {
    accessPermissionCheck(tenant, userAccountId);
    return userAccountViewService.getUserAccountAppendStaffAndOrganization(userAccountId);
  }

  @GetMapping("/current")
  @Operation(description = "登录账户详情")
  public UserAccount getUserAccount(@Principal Tenant tenant) {
    return getUserAccount(tenant, tenant.getId());
  }

  @GetMapping("/{userAccountId}:resourceDecideWhetherToDelete")
  @Operation(description = "获取影响删除账号相关的资源")
  public ResourceDecideWhetherToDeleteUserAccountResponse getResourceDecideWhetherToDelete(
          @Principal Tenant tenant,
          @PathVariable("userAccountId")  UserAccount userAccount) {
    long workOrderCount =
        workOrderRepository.countByProcessorUserAccountIdAndStatusIn(userAccount.getId(),Arrays.asList(AWAITING_PROCESSING, PROCESSING));
    return ResourceDecideWhetherToDeleteUserAccountResponse.builder()
            .haveWorkOrder(workOrderCount>0)
            .build();
  }
  @GetMapping("/{userAccountId}:resourceDecideWhetherToSwitch")
  @Operation(description = "获取影响开关账号相关的资源")

  public ResourceDecideWhetherToDeleteUserAccountResponse getResourceDecideWhetherToSwitch(
          @Principal Tenant tenant,
          @PathVariable("userAccountId")  UserAccount userAccount) {
    return getResourceDecideWhetherToDelete(tenant, userAccount);
  }

  @DeleteMapping("/{userAccountId}")
  @Operation(description = "删除账户")
  public void deleteUserAccount(
          @Principal Tenant tenant,
          @PathVariable("userAccountId")  UserAccount userAccount) {
    userAccountRepository.save(userAccount.setDeleted(true));
  }

  @PutMapping("/{userAccountId}")
  @Operation(description = "修改账户")
  public UserAccount changeUserAccount(
      @Principal Tenant tenant,
      @PathVariable long userAccountId,
      @RequestBody UserAccountValue userAccountValue) {
    accessPermissionCheck(tenant, userAccountId);
    if (StringUtils.isNotBlank(userAccountValue.getPassword())) {
      userAccountValue.setPassword(passwordEncoder.encode(userAccountValue.getPassword()));
    }
    return userAccountService.changeUserAccount(userAccountId, userAccountValue);
  }

  @PutMapping("/-:password")
  @Operation(description = "修改账户")
  public void changePassword(
      @Principal Tenant tenant,
      @Validated @RequestBody ChangePasswordRequest changePasswordRequest) {
    UserAccount userAccount = userAccountService.getUserAccount(tenant.getId());

    if (!passwordEncoder.matches(
        changePasswordRequest.getOldPassword(), userAccount.getPassword())) {
      throw new RuntimeException("原密码输入错误，请输入正确的原密码");
    }
    userAccount.changePassword(passwordEncoder,changePasswordRequest.getNewPassword());
    userAccountRepository.save(userAccount);
  }

  @PutMapping("/{userAccountId}:resetPassword")
  @Operation(description = "重置账户密码")
  public void resetPassword(
          @Principal Tenant tenant,
          @PathVariable("userAccountId") UserAccount userAccount,
          @RequestBody(required = false) UserAccountResetPassword userAccountResetPassword) {
    if (userAccountResetPassword == null) {
      userAccountResetPassword = new UserAccountResetPassword().setPassword("123456");
    }
    userAccount.changePassword(passwordEncoder, userAccountResetPassword.getPassword());
    userAccountRepository.save(userAccount);
  }

  @PutMapping("/{userAccountId}:switch")
  @Operation(description = "激活或禁用账号")
  public void switchUserAccount(
      @Principal Tenant tenant,
      @PathVariable long userAccountId,
      @Parameter(name = "enable", description = "enable=true->激活,enable=false，禁用") @RequestParam
          boolean enable) {
    accessPermissionCheck(tenant, userAccountId);
    userAccountService.switchUserAccount(userAccountId, enable);
  }

  @GetMapping
  @Operation(description = "账户列表")
  public IPage<UserAccount> listUserAccount(
      @Principal Tenant tenant,
      @Validated(value = BaseParam.BaseParamVaGroup.class) @ModelAttribute
          ListUserAccountRequest listUserAccountRequest) {
    return userAccountViewService.userAccountIPage(listUserAccountRequest);
  }

  @GetMapping("/staffName")
  @Operation(description = "账户对应的员工名称列表")
  public List<ListUserAccountIdAndStaffName> listUserAccountIdAndStaffName(@ModelAttribute UserAccountenabled userAccountenabled) {
    return userAccountViewService.listUserAccountIdAndStaffName(userAccountenabled);
  }

  private void accessPermissionCheck(Tenant tenant, long userAccountId) {
    //        if (!userAccountService.isAdminUserAccount(tenant.getId())) {
    //            if (tenant.getId() != userAccountId) {
    //                throw new AccessDeniedException("无权限操作");
    //            }
    //        }
  }
}
