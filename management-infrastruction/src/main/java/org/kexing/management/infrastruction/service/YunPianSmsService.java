package org.kexing.management.infrastruction.service;

import lombok.extern.slf4j.Slf4j;
import org.kexing.management.domin.service.SmsService;
import org.kexing.management.infrastruction.util.sms.YunPianSmsHttpClient;
import org.kexing.management.infrastruction.util.sms.response.YunPianSmsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.kexing.management.infrastruction.util.sms.YunPianSmsHttpClient.SMS_EXCEPTION_MSG;

/** @author lh */
@Service
@Slf4j
public class YunPianSmsService implements SmsService {
  @Value("${sms.enable}")
  private boolean enable;

  @Override
  public void sendSms(String mobile, String text) {
    if (enable) {
      YunPianSmsResponse yunPianSmsResponse = YunPianSmsHttpClient.sendSms(mobile, text);
      if (yunPianSmsResponse.getCode() != 0) {
        log.error(
            "发送短信异常:" + yunPianSmsResponse.getMsg() + ",code=" + yunPianSmsResponse.getCode());
        throw new RuntimeException(SMS_EXCEPTION_MSG);
      }
    }
  }
}
