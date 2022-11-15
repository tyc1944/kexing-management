package org.kexing.management.infrastruction.service.sql_server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.javatuples.Pair;
import org.kexing.management.domin.service.ErpDeviceRunStatusConfigService;
import org.kexing.management.domin.util.ErpDeviceUtil;
import org.kexing.management.infrastruction.query.dto.sql_server.ListErpDeviceRequest;
import org.kexing.management.infrastruction.query.dto.sql_server.ListErpDeviceResponse;
import org.kexing.management.infrastruction.query.dto.statistics.ErpDeviceStatusStatisticsResponse;
import org.kexing.management.infrastruction.repository.mybatis.sql_server.ErpDeviceQueryMapper;
import org.kexing.management.infrastruction.service.DeviceViewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.kexing.management.domin.util.ErpDeviceUtil.*;

/** @author lh */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceStatisticsViewService {
  private final DeviceViewService deviceViewService;
  private final ErpDeviceQueryMapper erpDeviceQueryMapper;
  private final HongXiangViewService hongXiangViewService;
  private final ErpDeviceRunStatusConfigService erpDeviceRunStatusConfigService;

  @Transactional(transactionManager = "sqlServerTransactionManager")
  public ErpDeviceStatusStatisticsResponse.ErpDiffDeviceTypeStatusStatisticsResponse getErpDevicesStatusStatistics() {
    CompletableFuture<ErpDeviceStatusStatisticsResponse> rxErpDeviceStatusStatisticsResponse =
        CompletableFuture.supplyAsync(
            () -> {
              ErpDeviceStatusStatisticsResponse erpDeviceStatusStatisticsResponse =
                  new ErpDeviceStatusStatisticsResponse();
              List<ListErpDeviceResponse> rxDevices =
                  erpDeviceQueryMapper.selectListErpDevice(
                      null,
                      null,
                      ListErpDeviceRequest.builder().erpDeviceType(ErpDeviceType.RX).build());

              rxDevices.forEach(
                  listErpDeviceResponse -> {
                    count(
                        erpDeviceStatusStatisticsResponse,
                        listErpDeviceResponse.getDeviceStatusId());
                  });
              return erpDeviceStatusStatisticsResponse;
            });

    CompletableFuture<ErpDeviceStatusStatisticsResponse>
        hongXinagErpDeviceStatusStatisticsResponse =
            CompletableFuture.supplyAsync(
                () -> {
                  ErpDeviceStatusStatisticsResponse erpDeviceStatusStatisticsResponse =
                      new ErpDeviceStatusStatisticsResponse();
                  List<ListErpDeviceResponse> hongxiangDevices =
                      erpDeviceQueryMapper.selectListErpDevice(
                          null,
                          HX_DEVICE_ID_NOT_CONTAIN,
                          ListErpDeviceRequest.builder().erpDeviceType(ErpDeviceType.HX).build());
                  if (CollectionUtils.isNotEmpty(hongxiangDevices)) {
                    double hongXinagTemperatureThresholdValue =
                        erpDeviceRunStatusConfigService.getHongXinagTemperatureThresholdValue();
                    hongxiangDevices
                        .forEach(
                            hongxiangDevice -> {
                              Optional<Double> currentTemperature =
                                  hongXiangViewService.getHongXiangCurrentTemperature(hongxiangDevice.getDeviceId());
                              String deviceStatusId =
                                  erpDeviceRunStatusConfigService.getDeviceStatusIdByDeviceStatus(
                                      erpDeviceRunStatusConfigService.getHongXiangDeviceStatus(
                                          currentTemperature, hongXinagTemperatureThresholdValue));
                              count(erpDeviceStatusStatisticsResponse, deviceStatusId);
                            });
                  }
                  return erpDeviceStatusStatisticsResponse;
                });

    CompletableFuture<ErpDeviceStatusStatisticsResponse> jzErpDeviceStatusStatisticsResponse =
        CompletableFuture.supplyAsync(
            () -> {
              ErpDeviceStatusStatisticsResponse erpDeviceStatusStatisticsResponse =
                  new ErpDeviceStatusStatisticsResponse();
              List<ListErpDeviceResponse> jzDevices =
                  erpDeviceQueryMapper.selectListErpDevice(
                      ErpDeviceUtil.jzMachNames,
                      null,
                      ListErpDeviceRequest.builder().erpDeviceType(ErpDeviceType.JZ).build());
              if (CollectionUtils.isNotEmpty(jzDevices)) {
                double jzVacuumThresholdValue =
                    erpDeviceRunStatusConfigService.getJZVacuumThresholdValue();
                jzDevices
                    .forEach(
                        listErpDeviceResponse -> {
                          Pair<String, String> statusIdAndDeviceStatus =
                              deviceViewService.getDeviceStatusIdAndDeviceStatus(
                                  jzVacuumThresholdValue, listErpDeviceResponse.getDeviceId());
                          count(
                              erpDeviceStatusStatisticsResponse,
                              statusIdAndDeviceStatus.getValue0());
                        });
              }
              return erpDeviceStatusStatisticsResponse;
            });

    return new ErpDeviceStatusStatisticsResponse.ErpDiffDeviceTypeStatusStatisticsResponse()
        .setHx(hongXinagErpDeviceStatusStatisticsResponse.join())
        .setJz(jzErpDeviceStatusStatisticsResponse.join())
        .setRx(rxErpDeviceStatusStatisticsResponse.join());
  }

  private void count(
      ErpDeviceStatusStatisticsResponse erpDeviceStatusStatisticsResponse, String deviceStatusId) {
    if (ObjectUtils.isNotEmpty(deviceStatusId)) {
      switch (deviceStatusId) {
        case DEVICE_WORK_STATUS_ID:
          erpDeviceStatusStatisticsResponse.addWorkNum(1);
          break;
        case DEVICE_DOWNTIME_STATUS_ID:
          erpDeviceStatusStatisticsResponse.addDownTimeNum(1);
          break;
      }
    }
  }
}
