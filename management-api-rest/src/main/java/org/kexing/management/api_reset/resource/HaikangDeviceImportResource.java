package org.kexing.management.api_reset.resource;

import com.alibaba.excel.EasyExcel;
import com.yunmo.iot.api.core.AssetEntityService;
import com.yunmo.iot.api.core.DeviceService;
import com.yunmo.iot.domain.assets.AssetEntity;
import com.yunmo.iot.domain.assets.value.AssetEntityValue;
import com.yunmo.iot.domain.core.Device;
import com.yunmo.iot.domain.core.ExcelReadListener;
import com.yunmo.iot.domain.core.value.DeviceValue;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.checkerframework.checker.units.qual.A;
import org.kexing.management.KeXingManagementApplication;
import org.kexing.management.api_reset.dto.HaikangDevice;
import org.kexing.management.domin.model.mysql.IotErpDeviceBindMap;
import org.kexing.management.domin.model.mysql.WorkOrder;
import org.kexing.management.domin.repository.mysql.IotErpDeviceBindMapRepository;
import org.kexing.management.infrastruction.service.DeviceViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "海康设备导入-摄像头，录像机，温湿度传感器，网关，测温传感器")
public class HaikangDeviceImportResource {

    @Autowired
    DeviceService deviceService;
    @Autowired
    AssetEntityService assetEntityService;
    @Autowired
    DeviceViewService deviceViewService;
    @Autowired
    IotErpDeviceBindMapRepository iotErpDeviceBindMapRepository;

    @SneakyThrows
    @Operation(description = "调用此接口前要先执行iot.sql")
    @PostMapping("/api/haikangdevice/import")
    public void importDevice(@RequestParam("file") MultipartFile file){
        List<HaikangDevice> list = new ArrayList<>();
        EasyExcel.read(file.getInputStream(), HaikangDevice.class,new ExcelReadListener(list)).sheet().doRead();
        Collections.sort(list,(HaikangDevice d1,HaikangDevice d2) -> d1.getType().compareTo(d2.getType()));
        list.forEach(l -> {
            Device device = new Device();
            device.setProjectId(KeXingManagementApplication.projectId);
            Map<String,Object> attributes = new HashMap<>();
            attributes.put("ip",l.getIp());
            attributes.put("port",l.getPort());
            attributes.put("deviceType",l.getType());
            attributes.put("location",l.getLocation());
            attributes.put("install_time",Instant.now().toEpochMilli());
            attributes.put("username",l.getUsername());
            attributes.put("password",l.getPassword());
            attributes.put("name",l.getName());
            attributes.put("doorType",l.getDoorType());
            attributes.put("deviceNo",l.getDeviceNo());
            attributes.put("cameraType",l.getCameraType());
            attributes.put("deviceSpec",l.getDeviceSpec());
            device.setAttributes(attributes);
            device.setLocalId(l.getName());
            device.setGateway(false);
            switch (WorkOrder.SourceType.valueOf(l.getType())){
                case camera:
                    attributes.put("rtsp",String.format("rtsp://%s:%s@%s:554/Streaming/Channels/101",l.getUsername(),l.getPassword(),l.getIp()));
                    device.setProductId(KeXingManagementApplication.cameraProductId);
                    device.setHubId(224915681743273985l);
                    Device device1 = deviceService.create(DeviceValue.from(device));
                    String workshopDevicesMacIdStr = l.getWorkshopDeviceMacIds();
                    if(StringUtils.isNotBlank(workshopDevicesMacIdStr)){
                        String[] workshopDevicesMacIds = workshopDevicesMacIdStr.split(",");
                        for (String workshopDevicesMacId : workshopDevicesMacIds) {
                            IotErpDeviceBindMap iotErpDeviceBindMap = IotErpDeviceBindMap.builder()
                                    .erpDeviceId(workshopDevicesMacId)
                                    .iotDeviceId(device1.getId())
                                    .build();
                            iotErpDeviceBindMapRepository.save(iotErpDeviceBindMap);
                        }
                    }
                    break;
                case video_recorder:
                    String videoRecorderBindCamerasStr = l.getVideoRecorderBindCameras();
                    if(StringUtils.isBlank(videoRecorderBindCamerasStr)){
                        throw new IllegalArgumentException(l.getIp()+"录像机绑定的摄像头不能为空");
                    }
                    device.setHubId(254915681743273985l);
                    device.setProductId(KeXingManagementApplication.videoRecorderProductId);
                    Device videoRecorderDevice = deviceService.create(DeviceValue.from(device));
                    AssetEntity assetEntity = new AssetEntity();
                    assetEntity.setDeviceId(videoRecorderDevice.getId());
                    assetEntity.setName(WorkOrder.SourceType.describe(WorkOrder.SourceType.video_recorder).concat("-").concat(l.getLocation()));
                    AssetEntity assetEntityVideoRecorder = assetEntityService.create(AssetEntityValue.from(assetEntity));
                    String[] videoRecorderBindCameras = videoRecorderBindCamerasStr.split(",");
                    for (String videoRecorderBindCamera : videoRecorderBindCameras) {
                        String ip = videoRecorderBindCamera.split(":")[0];
                        String channel = videoRecorderBindCamera.split(":")[1];
                        List<Device> devices = deviceService.findByAttributesProperty("ip",ip);
                        if(devices==null||devices.size()==0){
                            throw new IllegalArgumentException(videoRecorderDevice.getAttributes().get("ip")+"此摄像头IP不存在");
                        }
                        if(devices.size()>1){
                            throw new IllegalArgumentException(videoRecorderDevice.getAttributes().get("ip")+"此摄像头IP存在多个");
                        }
                        Device cameraDevice = devices.get(0);
                        cameraDevice.getAttributes().put("video_recoder_rtsp",String.format("rtsp://%s:%s@%s:554/Streaming/tracks/%s",l.getUsername(),l.getPassword(),l.getIp(),channel));
                        deviceService.save(cameraDevice);
                        AssetEntity assetEntity1 = new AssetEntity();
                        assetEntity1.setDeviceId(cameraDevice.getId());
                        assetEntity1.setName(WorkOrder.SourceType.describe(WorkOrder.SourceType.camera).concat("-").concat(cameraDevice.getAttributes().get("location").toString()));
                        assetEntity1.setParentId(assetEntityVideoRecorder.getId());
                        assetEntityService.create(AssetEntityValue.from(assetEntity1));
                    }
                    break;
                case gateway:
                    device.setHubId(264915681743273985l);
                    device.setProductId(KeXingManagementApplication.gatewayProductId);
                    Device gatewayDevice = deviceService.create(DeviceValue.from(device));
                    AssetEntity assetEntityGateway = new AssetEntity();
                    assetEntityGateway.setDeviceId(gatewayDevice.getId());
                    assetEntityGateway.setName(WorkOrder.SourceType.describe(WorkOrder.SourceType.gateway).concat("-").concat(l.getLocation()));
                    AssetEntity assetEntityGatewayDb = assetEntityService.create(AssetEntityValue.from(assetEntityGateway));
                    String gatewayBindIpsStr = l.getGatewayBindIps();
                    String[] gatewayBindIps = gatewayBindIpsStr.split(",");
                    for (String gatewayBindIp : gatewayBindIps) {
                        List<Device> devices = deviceService.findByAttributesProperty("ip",gatewayBindIp);
                        if(devices==null||devices.size()==0){
                            throw new IllegalArgumentException(gatewayDevice.getAttributes().get("ip")+"此设备IP不存在");
                        }
                        if(devices.size()>1){
                            throw new IllegalArgumentException("gatewayip:"+gatewayDevice.getAttributes().get("ip")+"_deviceip:"+gatewayBindIp+"此设备IP存在多个");
                        }
                        Device theDevice = devices.get(0);
                        AssetEntity assetEntityDevice = new AssetEntity();
                        assetEntityDevice.setDeviceId(theDevice.getId());
                        assetEntityDevice.setName(WorkOrder.SourceType.describe(WorkOrder.SourceType.camera).concat("-").concat(theDevice.getAttributes().get("location").toString()));
                        assetEntityDevice.setParentId(assetEntityGatewayDb.getId());
                        assetEntityService.create(AssetEntityValue.from(assetEntityDevice));
                    }
                    break;
                case temperature_sensor:
                    device.setHubId(244915681743273985l);
                    device.setProductId(KeXingManagementApplication.temperatureSensorProductId);
                    deviceService.create(DeviceValue.from(device));
                    break;
                case temperature_and_humidity_sensor:
                    device.setHubId(234915681743273985l);
                    device.setProductId(KeXingManagementApplication.temperatureAndHumiditySensorProductId);
                    deviceService.create(DeviceValue.from(device));
                    break;
            }
        });
    }
}
