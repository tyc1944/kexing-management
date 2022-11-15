package org.kexing.management.api_reset.resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yunmo.domain.common.Problems;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.kexing.management.domin.model.mysql.ErpDeviceConfig;
import org.kexing.management.domin.model.mysql.HongXiangExceptionReportedRecord;
import org.kexing.management.domin.model.mysql.jz.JzExceptionReportedRecord;
import org.kexing.management.domin.model.sql_server.JZWeiKeOldDataUploadDate;
import org.kexing.management.domin.repository.mysql.ErpDeviceConfigRepository;
import org.kexing.management.domin.service.ErpDeviceService;
import org.kexing.management.domin.util.ErpDeviceUtil;
import org.kexing.management.infrastruction.query.BaseParam;
import org.kexing.management.infrastruction.query.dto.ErpDeviceResponse;
import org.kexing.management.infrastruction.query.dto.ListExceptionReportedRecordRequest;
import org.kexing.management.infrastruction.query.dto.sql_server.*;
import org.kexing.management.infrastruction.query.dto.statistics.ErpDeviceStatusStatisticsResponse;
import org.kexing.management.infrastruction.repository.mybatis.sql_server.MacQueryMapper;
import org.kexing.management.infrastruction.service.DeviceViewService;
import org.kexing.management.infrastruction.service.sql_server.DeviceStatisticsViewService;
import org.kexing.management.infrastruction.service.sql_server.HongXiangViewService;
import org.kexing.management.infrastruction.service.sql_server.JZDeviceService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import javax.validation.groups.Default;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.kexing.management.domin.util.ErpDeviceUtil.*;
import static org.kexing.management.domin.util.JsonUtil.objectMapper;

/**
 * @author lh
 */
@RequestMapping("/api/erp/devices")
@RestController
@RequiredArgsConstructor
@Transactional
@Tag(name = "erp设别资源")
public class ErpDeviceResource {
  private final ProductionOrderResource productionOrderResource;
  private final ErpDeviceConfigRepository erpDeviceConfigRepository;
  private final HongXiangViewService hongXiangViewService;
  private final DeviceStatisticsViewService deviceStatisticsViewService;

  private final MacQueryMapper macQueryMapper;

  private final DeviceViewService deviceViewService;

  private final ErpDeviceService erpDeviceService;

  private final JZDeviceService jzDeviceService;

  @GetMapping
  @Operation(description = "erp设备信息(绕线机、烘箱、浇注机)")
  public IPage<ListErpDeviceResponse> listErpDevice(
      @Validated(value = BaseParam.BaseParamVaGroup.class) @ModelAttribute
          ListErpDeviceRequest listErpDeviceRequest) {
    return deviceViewService.listErpDevice(listErpDeviceRequest);
  }

  @GetMapping("/status/statistics")
  @Operation(description = "erp 设备工作、待机统计")
  public ErpDeviceStatusStatisticsResponse.ErpDiffDeviceTypeStatusStatisticsResponse getErpDevicesStatusStatistics() {
    return deviceStatisticsViewService.getErpDevicesStatusStatistics();
  }

  @GetMapping("/{deviceId}")
  @Operation(
      description = "erp设备详情(绕线机、烘箱、浇注机)",
      responses = {
        @ApiResponse(
            content = {
              @Content(
                  schema =
                      @Schema(
                          oneOf = {
                            GetErpDeviceResponse.class,
                            GetErpHongXiangDeviceResponse.class,
                            GetErpHRDeviceResponse.class,
                            GetErpJZHuNeiDeviceResponse.class,
                            GetErpJZHuWaiDeviceResponse.class,
                            GetErpJZWeiKeOldDeviceResponse.class,
                            GetErpJZWeiKeNewDeviceResponse.class
                          }))
            })
      })
  public GetErpDeviceResponse getErpDevice(@PathVariable String deviceId) {
    GetErpDeviceResponse getErpDeviceResponse = deviceViewService.getErpDevice(deviceId);

    if (!Objects.isNull(getErpDeviceResponse.getProductOrderNo())) {
      getErpDeviceResponse.setProductionOrderResponse(
          productionOrderResource.getProductionOrder(getErpDeviceResponse.getProductOrderNo()));
    }
    return getErpDeviceResponse;
  }

  @PostMapping("/hongxiang/{deviceId}/config")
  @Operation(description = "烘箱设备配置参数")
  @SneakyThrows
  public void configHongXiangDevice(
      @PathVariable String deviceId,
      @RequestBody @Valid ErpDeviceConfig.HongXiangDeviceConfig hongXiangDeviceConfig) {
    ErpDeviceResponse erpDeviceResponse = macQueryMapper.selectByMacId(deviceId);
    Problems.ensure(erpDeviceResponse != null, "设备不存在");

    Optional<ErpDeviceUtil.ErpDeviceType> erpDeviceType =
        ErpDeviceUtil.getDeviceType(deviceId, erpDeviceResponse.getDeviceName());
    if (erpDeviceType.isEmpty() || erpDeviceType.get() != ErpDeviceUtil.ErpDeviceType.HX) {
      throw Problem.valueOf(Status.BAD_REQUEST, "不支持当前设备配置参数");
    }

    List<String> allHongXiangBelongGroup =
        hongXiangViewService.getAllHongXiangBelongGroupBy(deviceId);
    if (CollectionUtils.isEmpty(allHongXiangBelongGroup)) {
      allHongXiangBelongGroup.add(deviceId);
    }
    for (String hongxiangDeviceId : allHongXiangBelongGroup) {
      Optional<ErpDeviceConfig> deviceConfig =
          erpDeviceConfigRepository.findByDeviceId(hongxiangDeviceId);
      String config = objectMapper.writeValueAsString(hongXiangDeviceConfig);
      erpDeviceConfigRepository.save(
          deviceConfig
              .map(erpDeviceConfig -> erpDeviceConfig.setConfig(config))
              .orElse(
                  ErpDeviceConfig.builder().deviceId(hongxiangDeviceId).config(config).build()));
    }
  }

  @PostMapping("/JZ/{deviceId}/config")
  @Operation(description = "浇注机设备配置参数")
  public void configJZDevice(
      @PathVariable String deviceId, @RequestBody @Valid ErpDeviceConfig.JZConfig jzConfig)
      throws JsonProcessingException {
    jzConfig.check();

    if (jzConfig instanceof ErpDeviceConfig.JZHuWaiDeviceConfig) {
      ErpDeviceResponse erpDeviceResponse = getErpDeviceResponse(deviceId, "浇注盈硕科户外不存在");
      Problems.ensure(erpDeviceResponse.getDeviceName().equals(HUWAI_NAME), "该设备非浇注盈硕科户外,无法配置");
      erpDeviceService.configDevice(
          erpDeviceResponse.getDeviceId(), objectMapper.writeValueAsString(jzConfig));
    } else if (jzConfig instanceof ErpDeviceConfig.JZHuNeiDeviceConfig) {
      ErpDeviceResponse erpDeviceResponse = getErpDeviceResponse(deviceId, "浇注盈硕科户内不存在");
      Problems.ensure(erpDeviceResponse.getDeviceName().equals(HUNEI_NAME), "该设备非浇注盈硕科户内设备,无法配置");
      erpDeviceService.configDevice(
          erpDeviceResponse.getDeviceId(), objectMapper.writeValueAsString(jzConfig));
    } else if (jzConfig instanceof ErpDeviceConfig.JZWeiKeOldDeviceConfig) {
      ErpDeviceResponse erpDeviceResponse = getErpDeviceResponse(deviceId, "浇注维克旧不存在");
      Problems.ensure(erpDeviceResponse.getDeviceName().equals(WEIOLD_NAME), "该设备非浇注维克旧设备,无法配置");
      erpDeviceService.configDevice(
          erpDeviceResponse.getDeviceId(), objectMapper.writeValueAsString(jzConfig));
    } else if (jzConfig instanceof ErpDeviceConfig.JZWeiKeNewDeviceConfig) {
      ErpDeviceResponse erpDeviceResponse = getErpDeviceResponse(deviceId, "浇注维克新不存在");
      Problems.ensure(erpDeviceResponse.getDeviceName().equals(WEINEW_NAME), "该设备非浇注维克新设备,无法配置");
      erpDeviceService.configDevice(
          erpDeviceResponse.getDeviceId(), objectMapper.writeValueAsString(jzConfig));
    } else {
      throw Problem.valueOf(Status.BAD_REQUEST, "当前浇注机设备,不支持的配置");
    }
  }

  private ErpDeviceResponse getErpDeviceResponse(String deviceId, String errorMessage) {
    ErpDeviceResponse erpDeviceResponse = macQueryMapper.selectByMacId(deviceId);
    Problems.ensure(erpDeviceResponse != null, errorMessage);
    return erpDeviceResponse;
  }

  @GetMapping("/JZ/{deviceId}/config")
  @Operation(description = "获取浇注盈硕科户内,设备配置参数")
  public ErpDeviceConfig.JZConfig getConfigDevice(@PathVariable String deviceId) {
    ErpDeviceResponse erpDeviceResponse = macQueryMapper.selectByMacId(deviceId);
    Problems.ensure(
        ObjectUtils.isNotEmpty(erpDeviceResponse)
            && ObjectUtils.isNotEmpty(erpDeviceResponse.getDeviceName()),
        "设备信息不存在");
    return erpDeviceService.getJZDeviceConfig(erpDeviceResponse.getDeviceId());
  }

  @GetMapping("/JZ/{deviceId}/exception/reported/records")
  public IPage<? extends JzExceptionReportedRecord> listJZExceptionReportedRecord(
      @PathVariable String deviceId,
      @Validated(value = BaseParam.BaseParamVaGroup.class) @ModelAttribute
          ListExceptionReportedRecordRequest listExceptionReportedRecord) {
    return deviceViewService.listJZExceptionReportedRecord(deviceId, listExceptionReportedRecord);
  }

  @GetMapping("/hongxiang/{deviceId}/config")
  public ErpDeviceConfig.HongXiangDeviceConfig getDeviceConfig(@PathVariable String deviceId) {
    return erpDeviceService.getHongXiangDeviceConfig(deviceId);
  }

  @GetMapping("/{deviceId}/production/records")
  @Operation(description = "erp设备生产记录(绕线机、烘箱、浇注机)")
  public IPage<ListErpDeviceProductRecordResponse> ListErpDeviceProductRecord(
      @PathVariable String deviceId,
      @Validated(value = {BaseParam.BaseParamVaGroup.class, Default.class}) @ModelAttribute
          ListErpDeviceProductRecordRequest listDeviceProductRecordRequest) {
    return deviceViewService.ListErpDeviceProductRecord(deviceId, listDeviceProductRecordRequest);
  }

  @GetMapping("/hongxiang/{deviceId}/exception/reported/records")
  @Operation(description = "erp设备异常上报记录(绕线机、烘箱、浇注机)")
  public IPage<HongXiangExceptionReportedRecord> listHongXiangExceptionReportedRecord(
      @PathVariable String deviceId,
      @Validated(value = BaseParam.BaseParamVaGroup.class) @ModelAttribute
          ListExceptionReportedRecordRequest listHongXiangExceptionReportedRecord) {
    return deviceViewService.listHongXiangExceptionReportedRecord(
        deviceId, listHongXiangExceptionReportedRecord);
  }
}
