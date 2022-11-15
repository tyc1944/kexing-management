package org.kexing.management.infrastruction.util.wechat.message_template;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class WechatMessage {
  private String touser;

  private WecahtData data;

  private String template_id;

  private String page;

  public interface WecahtData {}

  @Getter
  @Setter
  @Builder
  public static class MessageProperty {
    private String value;
  }
}
