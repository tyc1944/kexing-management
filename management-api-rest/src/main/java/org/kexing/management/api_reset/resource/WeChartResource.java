package org.kexing.management.api_reset.resource;

import com.yunmo.domain.common.Principal;
import com.yunmo.domain.common.Tenant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.kexing.management.infrastruction.service.WeChartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** @author lh */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class WeChartResource {

  private final WeChartService weChartService;

  @Value("${wechat.token}")
  private String wechatToken;

  @Value("${wechat.EncodingAESKey}")
  private String wechatEncodingAESKey;

  @PutMapping("/api/wechat/login")
  @Operation(description = "根据token获取")
  public String login(
      @Principal Tenant tenant,
      @Parameter(description = "微信login返回的code") @RequestParam String code) {
    long userAccountId = tenant.getId();

    return weChartService.login(userAccountId, code);
  }

  //  @GetMapping("/public/wechat/events")
  //  public String wechatEventVerify(
  //      @ModelAttribute WechatVerifyMessageSource wechatVerifyMessageSource) throws AesException {
  //    String signature =
  //        SHA1.getSHA1(
  //            wechatToken,
  //            wechatVerifyMessageSource.getTimestamp(),
  //            wechatVerifyMessageSource.getNonce(),
  //            "");
  //    if (signature.equals(wechatVerifyMessageSource.getSignature())) {
  //      return wechatVerifyMessageSource.getEchostr();
  //    }
  //    return null;
  //  }
}
