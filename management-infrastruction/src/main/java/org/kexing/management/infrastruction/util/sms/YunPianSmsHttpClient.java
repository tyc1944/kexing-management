package org.kexing.management.infrastruction.util.sms;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.kexing.management.infrastruction.util.sms.response.YunPianSmsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.kexing.management.domin.util.JsonUtil.objectMapper;

/** @author lh */
@Component
@Slf4j
public class YunPianSmsHttpClient {

  private static final String errMsgPrefix = "云片接口异常:";
  public static final String SMS_EXCEPTION_MSG = "短信发送异常，请检查相关数据。";
  private static String host;
  private static String apiKey;

  @SneakyThrows
  public static YunPianSmsResponse sendSms(String mobile, String text) {
    try {
      List<NameValuePair> nameValuePairs = new ArrayList<>();
      nameValuePairs.add(new BasicNameValuePair("apikey", apiKey));
      nameValuePairs.add(new BasicNameValuePair("text", text));
      nameValuePairs.add(new BasicNameValuePair("mobile", mobile));

      return objectMapper.readValue(
          sendPOST(host + "/v2/sms/single_send.json", nameValuePairs), YunPianSmsResponse.class);
    } catch (HttpResponseException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(SMS_EXCEPTION_MSG);
    }
  }

  private static String sendPOST(String url, List<NameValuePair> bodyJsonString)
      throws IOException {
    HttpPost httpPost = new HttpPost(url);

    return share(new UrlEncodedFormEntity(bodyJsonString, StandardCharsets.UTF_8), httpPost);
  }

  private static String share(
      HttpEntity httpEntity, HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase)
      throws IOException {
    if (httpEntity != null) {
      httpEntityEnclosingRequestBase.setEntity(httpEntity);
    }

    return baseShare(httpEntityEnclosingRequestBase);
  }

  private static String sendGet(String url) throws IOException {
    HttpGet httpGet = new HttpGet(url);
    return share(httpGet);
  }

  private static String share(HttpGet httpGet) throws IOException {
    return baseShare(httpGet);
  }

  private static String baseShare(HttpRequestBase httpRequestBase) throws IOException {
    try (CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpRequestBase)) {

      int statusCode = response.getStatusLine().getStatusCode();
      String responseString = EntityUtils.toString(response.getEntity());
      if (statusCode != 200) {
        log.error(responseString);
        throw new HttpResponseException(
            statusCode,
            String.valueOf(
                objectMapper
                    .readValue(responseString, Map.class)
                    .getOrDefault("errmsg", errMsgPrefix)));
      }
      log.info(responseString);
      return responseString;
    }
  }

  @Value("${sms.yunpian.host}")
  public void setHost(String host) {
    YunPianSmsHttpClient.host = host;
  }

  @Value("${sms.yunpian.apiKey}")
  public void setApiKey(String apiKey) {
    YunPianSmsHttpClient.apiKey = apiKey;
  }
}
