package org.kexing.management.infrastruction.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.kexing.management.domin.model.mysql.UserAccount;
import org.kexing.management.domin.repository.mysql.UserAccountRepository;
import org.kexing.management.domin.service.UserAccountService;
import org.kexing.management.infrastruction.util.wechat.WechatHttpClient;
import org.kexing.management.infrastruction.util.wechat.message_template.AssignWorkOrderMessageData;
import org.kexing.management.infrastruction.util.wechat.response.WechatErrorResponse;
import org.kexing.management.infrastruction.util.wechat.response.WechatLoginResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

/** @author lh */
@Service
@RequiredArgsConstructor
public class WeChartService {

  private final UserAccountService userAccountService;

  private final UserAccountRepository userAccountRepository;

  public String login(long userAccountId, String code) {
    WechatLoginResponse weChatLoginInfo = WechatHttpClient.getWechatLoginInfo(code);

    UserAccount userAccount = userAccountService.getUserAccount(userAccountId);

    userAccountRepository.save(
        userAccount.setWechatLogin(
            UserAccount.WechatLogin.builder()
                .openId(weChatLoginInfo.getOpenid())
                .sessionKey(weChatLoginInfo.getSession_key())
                .unionId(weChatLoginInfo.getUnionid())
                .build()));
    return weChatLoginInfo.getOpenid();
  }

  public boolean sendWorkOrderMessage(
      long userAccountId, AssignWorkOrderMessageData assignWorkOrderMessageData) {
    Optional<UserAccount> userAccountOp = userAccountRepository.findById(userAccountId);
    if (userAccountOp.isEmpty()) {
      return false;
    }
    UserAccount.WechatLogin wechatLogin = userAccountOp.get().getWechatLogin();
    if (ObjectUtils.isNotEmpty(wechatLogin) && StringUtils.isNotBlank(wechatLogin.getOpenId())) {
      WechatErrorResponse wechatErrorResponse =
          WechatHttpClient.sendMessage(wechatLogin.getOpenId(), assignWorkOrderMessageData);
      return wechatErrorResponse.getErrcode() == 0;
    }
    return false;
  }
}
