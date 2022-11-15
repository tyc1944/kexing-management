package org.kexing.management.infrastruction.repository.mybatis.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.domin.model.mysql.UserAccount;
import org.kexing.management.domin.model.mysql.value.UserAccountenabled;
import org.kexing.management.infrastruction.query.dto.ListUserAccountIdAndStaffName;
import org.kexing.management.infrastruction.query.dto.ListUserAccountRequest;

import java.util.List;

/** @author lh */
public interface UserAccountQueryMapper extends BaseMapper<UserAccount> {

  IPage<UserAccount> selectListUserAccountRequestPage(
      @Param("page") Page<?> page,
      @Param("listUserAccountRequest") ListUserAccountRequest listUserAccountRequest);

  UserAccount selectUserAccountAppendStaffAndOrganization(
      @Param("userAccountId") Long userAccountId);

  List<ListUserAccountIdAndStaffName> selectUserAccountIdAndStaffName(
          @Param("userAccountIds") Long[] userAccountIds, @Param("enabled") Boolean enabled);

}
