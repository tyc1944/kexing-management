package org.kexing.management.infrastruction.util.wechat.response;

import lombok.Getter;
import lombok.Setter;

/** @author lh */
@Getter
@Setter
public class WechatErrorResponse {
  private int errcode;
  private String errmsg;
}
