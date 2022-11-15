package org.kexing.management.infrastruction.util.wechat.message_template;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AssignWorkOrderMessageData implements WechatMessage.WecahtData {

  private WechatMessage.MessageProperty thing3;

  private WechatMessage.MessageProperty phone_number1;
}
