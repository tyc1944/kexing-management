package org.kexing.management.infrastruction.util.wechat.request;

import lombok.Getter;
import lombok.Setter;

/** @author lh */
@Getter
@Setter
public class WechatVerifyMessageSource {
  /** 微信加密签名 */
  private String signature;

  /** 时间戳 */
  private String timestamp;

  /** nonce */
  private String nonce;

  /** 随机字符串 */
  private String echostr;
}
