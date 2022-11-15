package org.kexing.management.domin.service;

import com.yunmo.domain.common.Problems;
import com.yunmo.iot.api.core.DeviceService;
import com.yunmo.iot.domain.core.Device;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.kexing.management.domin.model.mysql.DevicePictureMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class IotDeviceService {

    @Autowired
    DeviceService deviceService;

    public void updateDeviceAttribute(List<DevicePictureMap.DeviceLocation> locationList){
        List<Device> devices = new ArrayList<>();
    locationList.forEach(
        deviceLocation -> {
          long deviceId = Long.valueOf(deviceLocation.getDeviceId());
          Device device = deviceService.getDeviceById(deviceId);
          Problems.ensure(device != null, "设备id：" + deviceId + "对应的设备不存在");
          Map<String, Object> attributes = device.getAttributes();
          if (attributes == null) {
            attributes = new HashMap<>();
          }
          if (StringUtils.isNoneBlank(deviceLocation.getDeviceName())) {
              attributes.put("name", deviceLocation.getDeviceName());
          }
          attributes.put("deviceX", deviceLocation.getDeviceX());
          attributes.put("deviceY", deviceLocation.getDeviceY());
          attributes.put("deviceW", deviceLocation.getDeviceW());
          attributes.put("deviceH", deviceLocation.getDeviceH());
          device.setAttributes(attributes);
          devices.add(device);
        });
        deviceService.saveAll(devices);
    }
}
