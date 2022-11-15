package org.kexing.management.domin.service;

import com.yunmo.attendance.api.entity.Staff;
import com.yunmo.domain.common.Problems;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.kexing.management.domin.model.mysql.UserAccount;
import org.kexing.management.domin.model.mysql.value.UserAccountValue;
import org.kexing.management.domin.repository.mysql.StaffRepository;
import org.kexing.management.domin.repository.mysql.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/** @author lh */
@Service
@Transactional
@RequiredArgsConstructor
public class UserAccountService {
  private final UserAccountRepository userAccountRepository;
  private final StaffRepository staffRepository;

  public UserAccount createUserAccount(UserAccountValue userAccountValue) {
    Optional<UserAccount> userAccountOptional =
        userAccountRepository.findByAccountName(userAccountValue.getAccountName());
    if (userAccountOptional.isPresent()) {
      throw new EntityExistsException("账户已存在");
    }
    changeOrCreatUserAccountCheck(null, userAccountValue);

    return userAccountRepository.save(userAccountValue.create());
  }

  private void changeOrCreatUserAccountCheck(Long dbUserAccountId, UserAccountValue userAccountValue) {
    staffRepository
        .findById(userAccountValue.getStaffId())
        .orElseThrow(
            () -> {
              throw new EntityNotFoundException("员工信息不存在");
            });
    userAccountRepository
        .findByStaffId(userAccountValue.getStaffId())
        .ifPresent(
            userAccount -> {
              if (dbUserAccountId == null || !dbUserAccountId.equals(userAccount.getId())) {
                throw new EntityExistsException("员工信息已绑定");
              }
            });

    userAccountRepository
        .findByAccountName(userAccountValue.getAccountName())
        .ifPresent(
            userAccount -> {
              if (dbUserAccountId == null || !dbUserAccountId.equals(userAccount.getId())) {
                throw new EntityExistsException("账户名称已存在");
              }
            });
  }

  public UserAccount changeUserAccount(long userAccountId, UserAccountValue userAccountValue) {
    UserAccount userAccount = getUserAccount(userAccountId);
    changeOrCreatUserAccountCheck(userAccountId,userAccountValue);

    userAccount.changePermissionConf(userAccountValue.getPermissionConf());
    return userAccountRepository.save(userAccountValue.patchTo(userAccount));
  }

  public String getStaffName(long userAccountId) {
    return getStaff(userAccountId).getName();
  }

  public String getUserAccountMobile(long userAccountId) {
    return getStaff(userAccountId).getPhone();
  }

  private Staff getStaff(long userAccountId) {
    UserAccount userAccount = getUserAccount(userAccountId);
    Long staffId = userAccount.getStaffId();
    Problems.ensure(staffId != null, "账户员工id不能为空");
    Optional<Staff> staff = staffRepository.findById(staffId);
    Problems.ensure(staff.isPresent(), "账户的员工数据不存在");
    return staff.get();
  }

  public boolean isAdminUserAccount(long userAccountId) {
    return 1L == userAccountId;
  }

  public void switchUserAccount(long userAccountId, boolean enable) {
    UserAccount userAccount = getUserAccount(userAccountId);
    userAccountRepository.save(userAccount.setEnabled(enable));
  }

  public UserAccount getUserAccount(long userAccountId) {
    Optional<UserAccount> userAccountOptional = userAccountRepository.findById(userAccountId);
    if (userAccountOptional.isEmpty()) {
      throw new EntityNotFoundException("账户不存在");
    }
    return userAccountOptional.get();
  }

  public void checkValidUserAccount(Long... userAccountIds) {
    for (Long userAccountId : userAccountIds) {
      UserAccount userAccount = getUserAccount(userAccountId);
      String name = getStaffName(userAccountId);
      if (name == null) {
        name = "";
      }
      Problems.ensure(userAccount.isEnabled(), "员工:"+name+"的账户:"+userAccount.getAccountName() + "已禁用");
    }
  }
}
