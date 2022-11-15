package org.kexing.management.infrastruction.util.wechat;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.kexing.management.infrastruction.util.wechat.message_template.AssignWorkOrderMessageData;
import org.kexing.management.infrastruction.util.wechat.message_template.WechatMessage;
import org.kexing.management.infrastruction.util.wechat.response.WecahtAccessTokenResponse;
import org.kexing.management.infrastruction.util.wechat.response.WechatErrorResponse;
import org.kexing.management.infrastruction.util.wechat.response.WechatLoginResponse;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.kexing.management.domin.util.JsonUtil.objectMapper;

/** @author lh */
@Component
@Slf4j
public class WechatHttpClient {

  public static final String WECHAT_ACCESS_TOKEN = "wechat-accessToken";
  public static final String LOCK_WECHAT_ACCESS_TOKEN = "lock-wechat-accessToken";
  private static final String errMsgPrefix = "微信接口异常:";
  private static String host;
  private static String appId;
  private static String secret;
  private static String assignWorkOrderMessageTemplate;
  private static String weChatName;
  private static RedissonClient redissonClient;
  private static RedisTemplate<String, String> redisTemplate;

  @SneakyThrows
  public static WechatLoginResponse getWechatLoginInfo(String code) {
    try {
      WechatLoginResponse weChatLoginResponse =
          objectMapper.readValue(
              sendGet(
                  host
                      + "/sns/jscode2session"
                      + "?appid="
                      + appId
                      + "&secret="
                      + secret
                      + "&js_code="
                      + code
                      + "&grant_type=authorization_code"),
              WechatLoginResponse.class);
      if (weChatLoginResponse.getErrcode() != 0) {
        throw new RuntimeException(errMsgPrefix + weChatLoginResponse.getErrmsg());
      }
      return weChatLoginResponse;
    } catch (HttpResponseException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException("操作设备失败");
    }
  }

  @SneakyThrows
  public static String getAccessToken() {
    String weChatAccessToken = redisTemplate.opsForValue().get(WECHAT_ACCESS_TOKEN);
    if (ObjectUtils.isNotEmpty(weChatAccessToken)) {
      return weChatAccessToken;
    }

    RLock rLock = redissonClient.getLock(LOCK_WECHAT_ACCESS_TOKEN);
    boolean getdLock = rLock.tryLock(5, 2, TimeUnit.SECONDS);

    if (!getdLock) {
      throw new RuntimeException("获取锁:" + LOCK_WECHAT_ACCESS_TOKEN + "失败,请稍后尝试!");
    }

    try {
      weChatAccessToken = redisTemplate.opsForValue().get(WECHAT_ACCESS_TOKEN);
      if (ObjectUtils.isNotEmpty(weChatAccessToken)) {
        return weChatAccessToken;
      }

      WecahtAccessTokenResponse weCahtAccessTokenResponse =
          objectMapper.readValue(
              sendGet(
                  host
                      + "/cgi-bin/token"
                      + "?grant_type=client_credential"
                      + "&appid="
                      + appId
                      + "&secret="
                      + secret),
              WecahtAccessTokenResponse.class);
      if (weCahtAccessTokenResponse.getErrcode() != 0) {
        throw new RuntimeException(errMsgPrefix + weCahtAccessTokenResponse.getErrmsg());
      }
      redisTemplate
          .opsForValue()
          .set(
              WECHAT_ACCESS_TOKEN,
              weCahtAccessTokenResponse.getAccess_token(),
              weCahtAccessTokenResponse.getExpires_in(),
              TimeUnit.SECONDS);
      return weCahtAccessTokenResponse.getAccess_token();
    } catch (HttpResponseException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException("微信:accessToken接口异常");
    } finally {
      try {
        rLock.unlock(); // 自动释放锁后，异常忽略
      } catch (Exception ignored) {
      }
    }
  }

  @SneakyThrows
  public static WechatErrorResponse sendMessage(
      String touserId, WechatMessage.WecahtData wecahtData) {

    String templateId = null;
    if (wecahtData instanceof AssignWorkOrderMessageData) {
      templateId = assignWorkOrderMessageTemplate;
    }

    try {
      return objectMapper.readValue(
          sendPOST(
              host + "/cgi-bin/message/subscribe/send" + "?access_token=" + getAccessToken(),
              objectMapper.writeValueAsString(
                  WechatMessage.builder()
                      .touser(touserId)
                      .template_id(templateId)
                      .page(null) // TODO_LH: 2022/2/14 front offer
                      .data(wecahtData)
                      .build())),
          WechatErrorResponse.class);
    } catch (HttpResponseException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException("微信:发送消息接口异常");
    }
  }

  private static String sendPOST(String url, String bodyJsonString) throws IOException {
    HttpPost httpPost = new HttpPost(url);
    StringEntity jsonEntity = new StringEntity(bodyJsonString, "UTF-8");
    jsonEntity.setContentEncoding("UTF-8");

    return share(jsonEntity, httpPost);
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

  private static String sendPUT(String url, String bodyJsonString) throws IOException {
    HttpPut httpPut = new HttpPut(url);
    StringEntity jsonEntity = new StringEntity(bodyJsonString, "UTF-8");
    jsonEntity.setContentEncoding("UTF-8");

    return share(jsonEntity, httpPut);
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

  @Value("${wechat.appid}")
  public void setAppId(String appId) {
    WechatHttpClient.appId = appId;
  }

  @Value("${wechat.secret}")
  public void setSecret(String secret) {
    WechatHttpClient.secret = secret;
  }

  @Value("${wechat.host}")
  public void setHost(String host) {
    WechatHttpClient.host = host;
  }

  @Value("${wechat.assignWorkOrderMessageTemplate}")
  public void setAssignWorkOrderMessageTemplate(String assignWorkOrderMessageTemplate) {
    WechatHttpClient.assignWorkOrderMessageTemplate = assignWorkOrderMessageTemplate;
  }

  @Value("${wechat.name}")
  public void setWeChatName(String weChatName) {
    WechatHttpClient.weChatName = weChatName;
  }

  @Autowired
  public void setRedissonClient(RedissonClient redissonClient) {
    WechatHttpClient.redissonClient = redissonClient;
  }

  @Autowired
  public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
    WechatHttpClient.redisTemplate = redisTemplate;
  }

  public static String getWeChatName() {
    return weChatName;
  }
}
