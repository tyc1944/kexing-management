package org.kexing.management.infrastruction.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.kexing.management.domin.model.mysql.UserAccount;
import org.kexing.management.domin.model.mysql.value.UserAccountenabled;
import org.kexing.management.infrastruction.mapstruct.PageParamMapper;
import org.kexing.management.infrastruction.query.dto.ListUserAccountIdAndStaffName;
import org.kexing.management.infrastruction.query.dto.ListUserAccountRequest;
import org.kexing.management.infrastruction.repository.mybatis.mysql.UserAccountQueryMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author lh */
@Service
@RequiredArgsConstructor
public class UserAccountViewService {
  private final UserAccountQueryMapper userAccountQueryMapper;

  private final PageParamMapper pageParamMapper;

  public IPage<UserAccount> userAccountIPage(ListUserAccountRequest listUserAccountRequest) {
    Page<?> page = pageParamMapper.mapper(listUserAccountRequest);
    return userAccountQueryMapper.selectListUserAccountRequestPage(page, listUserAccountRequest);
  }

  public UserAccount getUserAccountAppendStaffAndOrganization(Long userAccountId) {
    return userAccountQueryMapper.selectUserAccountAppendStaffAndOrganization(userAccountId);
  }

  public Map<Long, String> userAccountIdAndStaffNamePair(Long... userAccountId) {
    return userAccountQueryMapper.selectUserAccountIdAndStaffName(userAccountId,null).stream()
        .collect(
            HashMap::new,
            (m, v) -> m.put(v.getUserAccountId(), v.getUserAccountStaffName()),
            HashMap::putAll);
  }

  public List<ListUserAccountIdAndStaffName> listUserAccountIdAndStaffName(UserAccountenabled userAccountenabled) {
    return userAccountQueryMapper.selectUserAccountIdAndStaffName(
        null, userAccountenabled.getEnabled());
  }
}
