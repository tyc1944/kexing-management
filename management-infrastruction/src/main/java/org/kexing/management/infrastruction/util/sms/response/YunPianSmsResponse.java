package org.kexing.management.infrastruction.util.sms.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YunPianSmsResponse {
  private String msg;

  private String unit;

  private Integer code;

  private Double fee;

  private Integer count;

  private String mobile;

  private Long sid;
}
