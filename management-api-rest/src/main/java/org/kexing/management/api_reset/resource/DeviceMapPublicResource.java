package org.kexing.management.api_reset.resource;

import com.yunmo.domain.common.Problems;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.kexing.management.domin.model.mysql.DevicePictureMap;
import org.kexing.management.domin.model.mysql.value.DevicePictureMapValue;
import org.kexing.management.domin.repository.mysql.DevicePictureMapRepository;
import org.kexing.management.domin.service.IotDeviceService;
import org.kexing.management.infrastruction.query.dto.ErpDeviceResponse;
import org.kexing.management.infrastruction.repository.mybatis.sql_server.MacQueryMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.kexing.management.domin.model.mysql.DevicePictureMap.Type.IOT_DEVICE_MAP;
import static org.kexing.management.domin.model.mysql.DevicePictureMap.Type.WORKSHOP_DEVICE_MAP;

/** @author lh */
@RestController
@RequestMapping("/public/devices")
@RequiredArgsConstructor
@Tag(name = "公共设备资源")
public class DeviceMapPublicResource {
  private final MacQueryMapper macQueryMapper;
  private final IotDeviceService iotDeviceService;
  private final DeviceResource deviceResource;
  private final DevicePictureMapRepository devicePictureMapRepository;

  public static void checkDeviceMapCount(List<DevicePictureMap> devicePictureMaps) {
    Problems.ensure(devicePictureMaps.size() <= 1, "设备地图信息数据存在多条");
  }

  @GetMapping("/map")
  @Operation(description = "获取设备地图信息")
  public DevicePictureMap getDeviceMap(@RequestParam(value = "type") DevicePictureMap.Type type) {
    return deviceResource.getDevicePictureMap(type);
  }

  @PostMapping("/map")
  @Operation(description = "设备地图信息修改")
  public void createDeviceMap(@RequestBody @Valid DevicePictureMapValue devicePictureMapValue) {
    DevicePictureMap.Type type = devicePictureMapValue.getType();
    Optional<DevicePictureMap> devicePictureMapOptional = devicePictureMapRepository.findByType(type);
    List<DevicePictureMap.DeviceLocation> deviceLocationList = devicePictureMapValue.getDeviceLocationList();

    //check
    if(CollectionUtils.isNotEmpty(deviceLocationList)){
      if (type == WORKSHOP_DEVICE_MAP) {
        deviceLocationList.stream()
            .collect(
                Collectors.groupingBy(
                    DevicePictureMap.DeviceLocation::getDeviceId, Collectors.counting()))
            .entrySet()
            .stream()
            .peek(
                stringLongEntry -> {
                  Problems.ensure(
                      stringLongEntry.getValue() <= 1, "设备id:" + stringLongEntry.getKey() + "存在重复");
                })
            .collect(Collectors.toList());
        for (DevicePictureMap.DeviceLocation deviceLocation : deviceLocationList) {
          ErpDeviceResponse erpDeviceResponse =
              macQueryMapper.selectByMacId(deviceLocation.getDeviceId());
          Problems.ensure(
              erpDeviceResponse != null, "erp设备id:" + deviceLocation.getDeviceId() + "不存在");
          deviceLocation.setDeviceName(deviceLocation.getDeviceName());
        }
      }
    }

    devicePictureMapOptional.ifPresentOrElse(
        devicePictureMap -> {
          devicePictureMapRepository.save(
              ignoreIOtDeviceLocation(devicePictureMapValue.assignTo(devicePictureMap)));
        },
        () ->
            devicePictureMapRepository.save(
                ignoreIOtDeviceLocation(devicePictureMapValue.create())));

    if (type == IOT_DEVICE_MAP) {
      iotDeviceService.updateDeviceAttribute(devicePictureMapValue.getDeviceLocationList());
    }
  }

  private DevicePictureMap ignoreIOtDeviceLocation(DevicePictureMap devicePictureMap) {
    if (devicePictureMap.getType() == IOT_DEVICE_MAP) {
       devicePictureMap.setDeviceLocationList(null);
    }
    return devicePictureMap;
  }
}
