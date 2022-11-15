package org.kexing.management.infrastruction.util.wechat.response;

import lombok.Data;

/** @author lh */
@Data
public class WechatLoginResponse {
  /** 账户唯一标识 */
  private String openid;
  /** 会话密钥 */
  private String session_key;
  /** 账户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。 */
  private String unionid;

  private int errcode;
  private String errmsg;
}
