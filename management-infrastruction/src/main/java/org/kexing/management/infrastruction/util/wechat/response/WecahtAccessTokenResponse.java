package org.kexing.management.infrastruction.util.wechat.response;

import lombok.Getter;
import lombok.Setter;

/** @author lh */
@Setter
@Getter
public class WecahtAccessTokenResponse extends WechatErrorResponse {
  private String access_token;
  private int expires_in;
}
