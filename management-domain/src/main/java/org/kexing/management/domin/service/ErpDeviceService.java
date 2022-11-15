package org.kexing.management.domin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kexing.management.domin.model.mysql.ErpDeviceConfig;
import org.kexing.management.domin.repository.mysql.ErpDeviceConfigRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.kexing.management.domin.util.ErpDeviceUtil.HONGXIANG_ID_PRE;
import static org.kexing.management.domin.util.JsonUtil.objectMapper;

/** @author lh */
@Service
@RequiredArgsConstructor
@Slf4j
public class ErpDeviceService {
  private final ErpDeviceConfigRepository erpDeviceConfigRepository;

  public ErpDeviceConfig.HongXiangDeviceConfig getHongXiangDeviceConfig(String deviceId) {
    Optional<ErpDeviceConfig> deviceConfig = erpDeviceConfigRepository.findByDeviceId(deviceId);
    return deviceConfig
        .map(
            erpDeviceConfig -> {
              try {
                return objectMapper.readValue(
                    erpDeviceConfig.getConfig(), ErpDeviceConfig.HongXiangDeviceConfig.class);
              } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
              }
              return null;
            })
        .orElse(null);
  }

  public List<ErpDeviceConfig> listHongXiangDeviceConfig() {
      return erpDeviceConfigRepository.findByDeviceIdStartsWithIgnoreCase(HONGXIANG_ID_PRE);
  }

  @SneakyThrows
  public void configDevice(String deviceId, String jzDeviceConfig) {
    Optional<ErpDeviceConfig> deviceConfig = erpDeviceConfigRepository.findByDeviceId(deviceId);
    erpDeviceConfigRepository.save(
        deviceConfig
            .map(erpDeviceConfig -> erpDeviceConfig.setConfig(jzDeviceConfig))
            .orElse(ErpDeviceConfig.builder().deviceId(deviceId).config(jzDeviceConfig).build()));
  }

  public ErpDeviceConfig.JZConfig  getJZDeviceConfig(String deviceId) {
    Optional<ErpDeviceConfig> deviceConfig = erpDeviceConfigRepository.findByDeviceId(deviceId);
    return deviceConfig
        .map(
            erpDeviceConfig -> {
              try {
                return objectMapper.readValue(
                    erpDeviceConfig.getConfig(), ErpDeviceConfig.JZConfig.class);
              } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
              }
              return null;
            })
        .orElse(null);
  }
}
