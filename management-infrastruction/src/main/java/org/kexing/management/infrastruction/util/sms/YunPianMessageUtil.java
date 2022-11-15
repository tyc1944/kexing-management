package org.kexing.management.infrastruction.util.sms;

import org.kexing.management.infrastruction.util.wechat.WechatHttpClient;

import java.text.MessageFormat;

/** @author lh */
public class YunPianMessageUtil {

  public static final String ASSIGN_WORKORDER_TEMPLATE =
      "【科兴电器】您有一条来自科兴电器待处理的工单（工单编号：{0}），请及时登录微信{1}小程序进行处理。";

  public static String getAssignWorkOrderText(String workOrderNo) {
    return MessageFormat.format(
        ASSIGN_WORKORDER_TEMPLATE, workOrderNo, WechatHttpClient.getWeChatName());
  }
}
