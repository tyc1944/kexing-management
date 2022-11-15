package org.kexing.management.infrastruction.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunmo.domain.common.Problems;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.javatuples.Pair;
import org.kexing.management.domin.model.mysql.DeviceSource;
import org.kexing.management.domin.model.mysql.HongXiangExceptionReportedRecord;
import org.kexing.management.domin.model.mysql.WorkOrder;
import org.kexing.management.domin.model.mysql.jz.JzExceptionReportedRecord;
import org.kexing.management.domin.model.sql_server.JZHuNeiDataUploadDate;
import org.kexing.management.domin.model.sql_server.JZHuWaiDataUploadDate;
import org.kexing.management.domin.model.sql_server.JZWeiKeNewDataUploadDate;
import org.kexing.management.domin.model.sql_server.JZWeiKeOldDataUploadDate;
import org.kexing.management.domin.service.ErpDeviceRunStatusConfigService;
import org.kexing.management.domin.service.ErpDeviceService;
import org.kexing.management.domin.util.ErpDeviceUtil;
import org.kexing.management.infrastruction.mapstruct.GetErpDeviceResponseMapping;
import org.kexing.management.infrastruction.mapstruct.ListErpDeviceResponseMapping;
import org.kexing.management.infrastruction.mapstruct.PageParamMapper;
import org.kexing.management.infrastruction.query.dto.*;
import org.kexing.management.infrastruction.query.dto.sql_server.*;
import org.kexing.management.infrastruction.query.dto.statistics.IotDeviceStatusStatisticsResponse;
import org.kexing.management.infrastruction.repository.mybatis.mysql.*;
import org.kexing.management.infrastruction.repository.mybatis.sql_server.ErpDeviceQueryMapper;
import org.kexing.management.infrastruction.repository.mybatis.sql_server.MacQueryMapper;
import org.kexing.management.infrastruction.service.sql_server.HongXiangViewService;
import org.kexing.management.infrastruction.service.sql_server.JZDeviceService;
import org.kexing.management.infrastruction.util.SqlServerTimeUtil;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.kexing.management.domin.model.mysql.DeviceSource.IOT;
import static org.kexing.management.domin.util.ErpDeviceUtil.*;

@Service
@RequiredArgsConstructor
public class DeviceViewService {
  private final JZDeviceService jzDeviceService;
  private final HongXiangExceptionReportedRecordQueryMapper
      hongXiangExceptionReportedRecordQueryMapper;
  private final JZHuNeiExceptionReportedRecordQueryMapper jzHuNeiExceptionReportedRecordQueryMapper;
  private final JZHuWaiExceptionReportedRecordQueryMapper jzHuWaiExceptionReportedRecordQueryMapper;
  private final JZWeiKeNewExceptionReportedRecordQueryMapper
      jzWeiKeNewExceptionReportedRecordQueryMapper;
  private final JZWeiKeOldExceptionReportedRecordQueryMapper
      jzWeiKeOldExceptionReportedRecordQueryMapper;
  private final ErpDeviceService erpDeviceService;
  private final GetErpDeviceResponseMapping getErpDeviceResponseMapping;
  private final ListErpDeviceResponseMapping listErpDeviceResponseMapping;
  private final ErpDeviceQueryMapper erpDeviceQueryMapper;
  private final PageParamMapper pageParamMapper;
  private final DeviceQueryMapper deviceQueryMapper;
  private final MacQueryMapper macQueryMapper;
  private final HongXiangViewService hongXiangViewService;
  private final ErpDeviceRunStatusConfigService erpDeviceRunStatusConfigService;

  public IPage<IotDeviceWrap> listDevice(ListDeviceRequest listDeviceRequest) {
    Page page = pageParamMapper.mapper(listDeviceRequest);
    return deviceQueryMapper.selectListDeviceRequestPage(page, listDeviceRequest);
  }

  public List<ErpDeviceResponse> listDeviceLocation() {
    List<DeviceNameAndAttributesResponse> deviceNameAndAttributesRespons =
        deviceQueryMapper.selectAllDevice();
    List<ErpDeviceResponse> deviceLocationResponses =
        deviceNameAndAttributesRespons.stream()
            .map(
                item -> {
                  return ErpDeviceResponse.builder()
                      .deviceId(item.getDeviceId())
                      .location(getIotLocation(item))
                      .source(IOT)
                      .deviceName(getIotDeviceName(item))
                      .build();
                })
            .collect(Collectors.toList());

    deviceLocationResponses.addAll(macQueryMapper.selectMacs());
    return deviceLocationResponses;
  }

  private String getIotLocation(DeviceNameAndAttributesResponse deviceLocationResponse) {
    if (deviceLocationResponse != null) {
      Map<String, Object> attributes = deviceLocationResponse.getAttributes();
      if (attributes != null) {
        return MapUtils.getString(attributes, "location");
      }
    }
    return null;
  }

  private String getIotDeviceName(DeviceNameAndAttributesResponse deviceLocationResponse) {
    if (deviceLocationResponse != null) {
      Map<String, Object> attributes = deviceLocationResponse.getAttributes();
      if (attributes != null) {
        return MapUtils.getString(attributes, "name");
      }
    }
    return null;
  }

  public String getDeviceName(String deviceId, DeviceSource deviceSource) {
    switch (deviceSource) {
      case IOT:
        DeviceNameAndAttributesResponse deviceNameAndAttributesResponses =
            deviceQueryMapper.selectByDeviceId(Long.valueOf(deviceId));
        return ObjectUtils.isNotEmpty(deviceNameAndAttributesResponses)
            ? getIotDeviceName(deviceNameAndAttributesResponses)
            : null;
      case ERP:
        ErpDeviceResponse deviceLocationResponse = macQueryMapper.selectByMacId(deviceId);
        return ObjectUtils.isNotEmpty(deviceLocationResponse)
            ? deviceLocationResponse.getDeviceName()
            : null;
      default:
        throw Problem.valueOf(Status.BAD_REQUEST, "不支持的设备来源类型");
    }
  }

  public String getDeviceLocation(String deviceId, DeviceSource deviceSource) {
    switch (deviceSource) {
      case IOT:
        DeviceNameAndAttributesResponse deviceNameAndAttributesResponses =
            deviceQueryMapper.selectByDeviceId(Long.valueOf(deviceId));
        return ObjectUtils.isNotEmpty(deviceNameAndAttributesResponses)
            ? getIotLocation(deviceNameAndAttributesResponses)
            : null;
      case ERP:
        ErpDeviceResponse deviceLocationResponse = macQueryMapper.selectByMacId(deviceId);
        return ObjectUtils.isNotEmpty(deviceLocationResponse)
            ? deviceLocationResponse.getLocation()
            : null;
      default:
        throw Problem.valueOf(Status.BAD_REQUEST, "不支持的设备来源类型");
    }
  }

  public IPage<ListErpDeviceResponse> listErpDevice(ListErpDeviceRequest listErpDeviceRequest) {
    List<String> hxDeviceIdNotContain = null;
    List<String> jzMachNames = null;
    switch (listErpDeviceRequest.getErpDeviceType()) {
      case JZ:
        jzMachNames = ErpDeviceUtil.jzMachNames;
        break;
      case HX:
        hxDeviceIdNotContain = HX_DEVICE_ID_NOT_CONTAIN;
        break;
      case RX:
        break;
      default:
        throw Problem.valueOf(Status.BAD_REQUEST, "不支持的设备开头标识");
    }

    Page page = pageParamMapper.mapper(listErpDeviceRequest);
    IPage<ListErpDeviceResponse> listErpDeviceResponseIPage =
        erpDeviceQueryMapper.selectListErpDevice(
            page, jzMachNames, hxDeviceIdNotContain, listErpDeviceRequest);

    if (CollectionUtils.isNotEmpty(listErpDeviceResponseIPage.getRecords())) {
      switch (listErpDeviceRequest.getErpDeviceType()) {
        case HX:
          double hongXinagTemperatureThresholdValue =
              erpDeviceRunStatusConfigService.getHongXinagTemperatureThresholdValue();

          listErpDeviceResponseIPage.setRecords(
              listErpDeviceResponseIPage.getRecords().stream()
                  .map(
                      listErpDeviceResponse -> {
                        Optional<Double> currentTemperature =
                            hongXiangViewService.getHongXiangCurrentTemperature(
                                listErpDeviceResponse.getDeviceId());

                        ListErpHongXiangDeviceResponse listErpHongXiangDeviceResponse =
                            listErpDeviceResponseMapping.createByListErpDeviceResponse(
                                listErpDeviceResponse,
                                erpDeviceService.getHongXiangDeviceConfig(
                                    listErpDeviceResponse.getDeviceId()),
                                currentTemperature.orElse(null));
                        listErpHongXiangDeviceResponse.setDeviceStatus(
                            erpDeviceRunStatusConfigService.getHongXiangDeviceStatus(
                                currentTemperature, hongXinagTemperatureThresholdValue));
                        listErpHongXiangDeviceResponse.setDeviceStatusId(
                            erpDeviceRunStatusConfigService.getDeviceStatusIdByDeviceStatus(
                                listErpDeviceResponse.getDeviceStatus()));
                        return listErpHongXiangDeviceResponse;
                      })
                  .collect(Collectors.toList()));
          break;
        case JZ:
          double jzVacuumThresholdValue =
              erpDeviceRunStatusConfigService.getJZVacuumThresholdValue();
          listErpDeviceResponseIPage.setRecords(
              listErpDeviceResponseIPage.getRecords().stream()
                  .map(
                      listErpDeviceResponse ->
                          setDeviceStatus(jzVacuumThresholdValue, listErpDeviceResponse))
                  .collect(Collectors.toList()));
          break;
      }
    }
    return listErpDeviceResponseIPage;
  }

  private ListErpDeviceResponse setDeviceStatus(
      double jzVacuumThresholdValue, ListErpDeviceResponse listErpDeviceResponse) {
    Pair<String, String> statusIdAndDeviceStatus =
        getDeviceStatusIdAndDeviceStatus(
            jzVacuumThresholdValue, listErpDeviceResponse.getDeviceId());
    listErpDeviceResponse.setDeviceStatusId(statusIdAndDeviceStatus.getValue0());
    listErpDeviceResponse.setDeviceStatus(statusIdAndDeviceStatus.getValue1());
    return listErpDeviceResponse;
  }

  public Pair<String, String> getDeviceStatusIdAndDeviceStatus(
      double jzVacuumThresholdValue, String jzDeviceId) {
    GetErpDeviceResponse getErpDeviceResponse = erpDeviceQueryMapper.selectErpDevice(jzDeviceId);
    String deviceName = getErpDeviceResponse.getDeviceName();
    Pair<Instant, Double> vacuumPouringJarA = null;
    Pair<Instant, Double> vacuumPouringJarB = null;

    if (WEIOLD_NAME.equals(deviceName)) {
      List<JZWeiKeOldDataUploadDate> jzWeiKeOldDataUploadDates =
          jzDeviceService.getLastJZWeiKeOldDataUploadDate(jzDeviceId);

      for (JZWeiKeOldDataUploadDate jzWeiKeOldDataUploadDate : jzWeiKeOldDataUploadDates) {
        if (jzWeiKeOldDataUploadDate.getPouringJarAVacuum() != null) {
          vacuumPouringJarA =
              Pair.with(
                  jzWeiKeOldDataUploadDate.getCredate(),
                  jzWeiKeOldDataUploadDate.getPouringJarAVacuum());
        }
        if (jzWeiKeOldDataUploadDate.getPouringJarBVacuum() != null) {
          vacuumPouringJarB =
              Pair.with(
                  jzWeiKeOldDataUploadDate.getCredate(),
                  jzWeiKeOldDataUploadDate.getPouringJarBVacuum());
        }
      }
    } else if (HUNEI_NAME.equals(deviceName)) {
      List<JZHuNeiDataUploadDate> jzHuNeiDataUploadDate =
          jzDeviceService.getLastJZHuNeiDataUploadDate(jzDeviceId);
      for (JZHuNeiDataUploadDate huNeiDataUploadDate : jzHuNeiDataUploadDate) {
        if (huNeiDataUploadDate.getPouringJarAVacuum() != null) {
          vacuumPouringJarA =
              Pair.with(
                  huNeiDataUploadDate.getCredate(), huNeiDataUploadDate.getPouringJarAVacuum());
        }
        if (huNeiDataUploadDate.getPouringJarBVacuum() != null) {
          vacuumPouringJarB =
              Pair.with(
                  huNeiDataUploadDate.getCredate(), huNeiDataUploadDate.getPouringJarBVacuum());
        }
      }
    } else if (HUWAI_NAME.equals(deviceName)) {
      List<JZHuWaiDataUploadDate> jzHuWaiDataUploadDates =
          jzDeviceService.getLastJZHuWaiDataUploadDate(jzDeviceId);
      for (JZHuWaiDataUploadDate jzHuWaiDataUploadDate : jzHuWaiDataUploadDates) {
        if (jzHuWaiDataUploadDate.getPouringJarAVacuum() != null) {
          vacuumPouringJarA =
              Pair.with(
                  jzHuWaiDataUploadDate.getCredate(), jzHuWaiDataUploadDate.getPouringJarAVacuum());
        }
      }
    } else if (WEINEW_NAME.equals(deviceName)) {
      List<JZWeiKeNewDataUploadDate> jzWeiKeNewDataUploadDates =
          jzDeviceService.getLastJZWeiKeNewDataUploadDate(jzDeviceId);
      for (JZWeiKeNewDataUploadDate jzWeiKeNewDataUploadDate : jzWeiKeNewDataUploadDates) {
        if (jzWeiKeNewDataUploadDate.getPouringJarAVacuum() != null) {
          vacuumPouringJarA =
              Pair.with(
                  jzWeiKeNewDataUploadDate.getCredate(),
                  jzWeiKeNewDataUploadDate.getPouringJarAVacuum());
        }
        if (jzWeiKeNewDataUploadDate.getPouringJarBVacuum() != null) {
          vacuumPouringJarB =
              Pair.with(
                  jzWeiKeNewDataUploadDate.getCredate(),
                  jzWeiKeNewDataUploadDate.getPouringJarBVacuum());
        }
      }
    }

    String deviceStatus =
        erpDeviceRunStatusConfigService.getJZDeviceStatus(
            vacuumPouringJarA, vacuumPouringJarB, jzVacuumThresholdValue);

    String deviceStatusId =
        erpDeviceRunStatusConfigService.getDeviceStatusIdByDeviceStatus(deviceStatus);
    return Pair.with(deviceStatusId, deviceStatus);
  }

  public GetErpDeviceResponse getErpDevice(String deviceId) {
    GetErpDeviceResponse getErpDeviceResponse = erpDeviceQueryMapper.selectErpDevice(deviceId);
    if (getErpDeviceResponse == null) {
      throw Problem.valueOf(Status.INTERNAL_SERVER_ERROR, deviceId + "设备不存在");
    }
    getErpDeviceResponse.setStartProductionDateTime(
        SqlServerTimeUtil.columnTimeAddToDate(
            getErpDeviceResponse.getStartProductionDateTime(),
            getErpDeviceResponse.getStartProductionTime()));

    Optional<ErpDeviceType> erpDeviceType =
        ErpDeviceUtil.getDeviceType(
            getErpDeviceResponse.getDeviceId(), getErpDeviceResponse.getDeviceName());
    if (erpDeviceType.isEmpty()) {
      return getErpDeviceResponseMapping.createByGetErpDeviceResponse(
          getErpDeviceResponse,
          getDeviceCraftMaterials(
              getErpDeviceResponse.getProductNumber(), getErpDeviceResponse.getPrcId()));
    }

    switch (erpDeviceType.get()) {
      case HX:
        Optional<Double> currentTemperature =
            hongXiangViewService.getHongXiangCurrentTemperature(deviceId);
        GetErpHongXiangDeviceResponse erpHongXiangDeviceResponse =
            getErpDeviceResponseMapping.createByGetErpDeviceResponse(
                getErpDeviceResponse, currentTemperature.orElse(null));
        erpHongXiangDeviceResponse.setDeviceStatus(
            erpDeviceRunStatusConfigService.getHongXiangDeviceStatus(currentTemperature));
        erpHongXiangDeviceResponse.setHongXiangDeviceConfig(
            erpDeviceService.getHongXiangDeviceConfig(deviceId));

        List<String> allHongXiangBelongGroup =
            hongXiangViewService.getAllHongXiangBelongGroupBy(deviceId);
        if (org.springframework.util.CollectionUtils.isEmpty(allHongXiangBelongGroup)) {
          allHongXiangBelongGroup.add(deviceId);
        }
        erpHongXiangDeviceResponse.setHongXiangGroup(
            allHongXiangBelongGroup.stream()
                .map(erpDeviceQueryMapper::selectErpDevice)
                .filter(item -> !Objects.isNull(item))
                .collect(Collectors.toList()));
        return erpHongXiangDeviceResponse;
      case JZ:
        double jzVacuumThresholdValue = erpDeviceRunStatusConfigService.getJZVacuumThresholdValue();
        setDeviceStatus(jzVacuumThresholdValue, getErpDeviceResponse);

        Pair<String, String> statusIdAndDeviceStatus =
            getDeviceStatusIdAndDeviceStatus(
                jzVacuumThresholdValue, getErpDeviceResponse.getDeviceId());
        switch (getErpDeviceResponse.getDeviceName()) {
          case HUNEI_NAME:
            getErpDeviceResponse =
                getErpDeviceResponseMapping.createByGetErpDeviceResponse(
                    getErpDeviceResponse,
                    jzDeviceService.getTodayLastJZHuNeiDataUploadDate(deviceId));
            break;
          case HUWAI_NAME:
            getErpDeviceResponse =
                getErpDeviceResponseMapping.createByGetErpDeviceResponse(
                    getErpDeviceResponse,
                    jzDeviceService.getTodayLastJZHuWaiDataUploadDate(deviceId));
            break;
          case WEINEW_NAME:
            getErpDeviceResponse =
                getErpDeviceResponseMapping.createByGetErpDeviceResponse(
                    getErpDeviceResponse,
                    jzDeviceService.getTodayLastJZWeiKeNewDataUploadDate(deviceId));
            break;
          case WEIOLD_NAME:
            getErpDeviceResponse =
                getErpDeviceResponseMapping.createByGetErpDeviceResponse(
                    getErpDeviceResponse,
                    jzDeviceService.getTodayLastJZWeiKeOldDataUploadDate(deviceId));
            break;
        }
        getErpDeviceResponse.setDeviceStatusId(statusIdAndDeviceStatus.getValue0());
        getErpDeviceResponse.setDeviceStatus(statusIdAndDeviceStatus.getValue1());
        return getErpDeviceResponse;
      case RX:
        return getErpDeviceResponseMapping.createByGetErpDeviceResponse(
            getErpDeviceResponse,
            getDeviceCraftMaterials(
                getErpDeviceResponse.getProductNumber(), getErpDeviceResponse.getPrcId()));
      default:
        return getErpDeviceResponse;
    }
  }

  private List<MeterialResponse> getDeviceCraftMaterials(String productNumber, String prcId) {
    return erpDeviceQueryMapper.selectCraftMaterial(productNumber, prcId);
  }

  public IPage<ListErpDeviceProductRecordResponse> ListErpDeviceProductRecord(
      String deviceId, ListErpDeviceProductRecordRequest listDeviceProductRecordRequest) {
    Page page = pageParamMapper.mapper(listDeviceProductRecordRequest);
    IPage<ListErpDeviceProductRecordResponse> listErpDeviceProductRecordResponseIPage =
        erpDeviceQueryMapper.selectListErpDeviceProductRecord(
            page, deviceId, listDeviceProductRecordRequest);
    if (CollectionUtils.isNotEmpty(listErpDeviceProductRecordResponseIPage.getRecords())) {
      for (ListErpDeviceProductRecordResponse listErpDeviceProductRecordResponse :
          listErpDeviceProductRecordResponseIPage.getRecords()) {
        listErpDeviceProductRecordResponse.setStartProductionDateTime(
            SqlServerTimeUtil.columnTimeAddToDate(
                listErpDeviceProductRecordResponse.getStartProductionDateTime(),
                listErpDeviceProductRecordResponse.getStartProductionTime()));
      }
    }

    return listErpDeviceProductRecordResponseIPage;
  }

  public IPage<HongXiangExceptionReportedRecord> listHongXiangExceptionReportedRecord(
      String deviceId, ListExceptionReportedRecordRequest listHongXiangExceptionReportedRecord) {
    Page page = pageParamMapper.mapper(listHongXiangExceptionReportedRecord);
    return hongXiangExceptionReportedRecordQueryMapper
        .selectListHongXiangExceptionReportedRecordRequestPage(
            page, deviceId, listHongXiangExceptionReportedRecord);
  }

  public IPage<? extends JzExceptionReportedRecord> listJZExceptionReportedRecord(
      String deviceId, ListExceptionReportedRecordRequest listExceptionReportedRecord) {
    ErpDeviceResponse erpDeviceResponse = macQueryMapper.selectByMacId(deviceId);
    Problems.ensure(
        ObjectUtils.isNotEmpty(erpDeviceResponse)
            && ObjectUtils.isNotEmpty(erpDeviceResponse.getDeviceName()),
        "设备信息不存在");
    Page page = pageParamMapper.mapper(listExceptionReportedRecord);

    String deviceName = erpDeviceResponse.getDeviceName();
    if (HUNEI_NAME.equals(deviceName)) {
      return jzHuNeiExceptionReportedRecordQueryMapper.selectListExceptionReportedRecordRequestPage(
          page, deviceId, listExceptionReportedRecord);
    } else if (HUWAI_NAME.equals(deviceName)) {
      return jzHuWaiExceptionReportedRecordQueryMapper.selectListExceptionReportedRecordRequestPage(
          page, deviceId, listExceptionReportedRecord);
    } else if (WEIOLD_NAME.equals(deviceName)) {
      return jzWeiKeOldExceptionReportedRecordQueryMapper
          .selectListExceptionReportedRecordRequestPage(
              page, deviceId, listExceptionReportedRecord);
    } else if (WEINEW_NAME.equals(deviceName)) {
      return jzWeiKeNewExceptionReportedRecordQueryMapper
          .selectListExceptionReportedRecordRequestPage(
              page, deviceId, listExceptionReportedRecord);
    } else {
      throw Problem.valueOf(Status.BAD_REQUEST, "设备不支持");
    }
  }

  public IotDeviceStatusStatisticsResponse.IotDiffDeviceTypeStatusStatisticsResponse getIotDeviceStatusStatistics() {
    IotDeviceStatusStatisticsResponse camera =
        deviceQueryMapper.selectOnlineAndOfflineStatisticsByDeviceType(WorkOrder.SourceType.camera);
    IotDeviceStatusStatisticsResponse temperatureAndHumiditySensor =
        deviceQueryMapper.selectOnlineAndOfflineStatisticsByDeviceType(
            WorkOrder.SourceType.temperature_and_humidity_sensor);

    IotDeviceStatusStatisticsResponse temperatureSensor =
        deviceQueryMapper.selectOnlineAndOfflineStatisticsByDeviceType(
            WorkOrder.SourceType.temperature_sensor);

    IotDeviceStatusStatisticsResponse doorFace =
        deviceQueryMapper.selectOnlineAndOfflineStatisticsByDeviceType(
            WorkOrder.SourceType.door_face);

    IotDeviceStatusStatisticsResponse gateway =
        deviceQueryMapper.selectOnlineAndOfflineStatisticsByDeviceType(
            WorkOrder.SourceType.gateway);

    IotDeviceStatusStatisticsResponse robot =
            deviceQueryMapper.selectOnlineAndOfflineStatisticsByDeviceType(
                    WorkOrder.SourceType.robot);

    IotDeviceStatusStatisticsResponse.IotDiffDeviceTypeStatusStatisticsResponse iotDiffDeviceTypeStatusStatisticsResponse =
        new IotDeviceStatusStatisticsResponse.IotDiffDeviceTypeStatusStatisticsResponse();
     return iotDiffDeviceTypeStatusStatisticsResponse
        .setCamera(camera)
        .setTemperatureAndHumiditySensor(temperatureAndHumiditySensor)
        .setTemperatureSensor(temperatureSensor)
        .setDoorFace(doorFace)
        .setGateway(gateway)
        .setRobot(robot);
  }

  public DeviceNameAndAttributesResponse getDeviceMaterialCamera(
      List<DeviceNameAndAttributesResponse> deviceNameAndAttributesList) {
    DeviceNameAndAttributesResponse MaterialCameraDevice = null;
    for (DeviceNameAndAttributesResponse deviceNameAndAttributesResponse :
        deviceNameAndAttributesList) {
      if (deviceNameAndAttributesResponse.getAttributes().get("deviceType") != null
          && deviceNameAndAttributesResponse.getAttributes().get("deviceType").equals("camera")
          && deviceNameAndAttributesResponse.getAttributes().get("warehouse") != null
          && deviceNameAndAttributesResponse.getAttributes().get("warehouse").equals(true)) {
        MaterialCameraDevice = deviceNameAndAttributesResponse;
        break;
      }
    }
    return MaterialCameraDevice;
  }
}
