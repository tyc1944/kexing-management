package org.kexing.management.test;

import com.alibaba.excel.EasyExcel;
import com.yunmo.iot.api.core.AssetEntityService;
import com.yunmo.iot.api.core.DeviceService;
import com.yunmo.iot.api.core.TelemetryRecordService;
import com.yunmo.iot.domain.assets.AssetEntity;
import com.yunmo.iot.domain.core.Device;
import com.yunmo.iot.domain.core.ExcelReadListener;
import org.junit.jupiter.api.Test;
import org.kexing.management.KeXingManagementApplication;
import org.kexing.management.api_reset.dto.HaikangDevice;
import org.kexing.management.domin.model.mysql.WorkOrder;
import org.kexing.management.domin.service.CameraAlertService;
import org.kexing.management.domin.service.cameraalert.AlertMessage;
import org.kexing.management.domin.service.cameraalert.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = KeXingManagementApplication.class)
public class ApplicationTest {

    @Autowired
    TelemetryRecordService telemetryRecordService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    AssetEntityService assetEntityService;
    @Autowired
    CameraAlertService cameraAlertService;

    @Test
    public void cameraAlertWorkOrder(){
        long deviceId = 319297901861797890l;
        AlertMessage alertMessage = AlertMessage.builder()
                        .alert_rtsp("rtsp://admin:yunmotec123456@192.168.102.94:554/Streaming/Channels/102")
                        .alert_time(LocalDateTime.of(2022,5,5,12,34).atZone(ZoneId.systemDefault()).toEpochSecond())
                        .boundary(false)
                        .emp_ids("C2514")
                        .event_type(EventType.people_broke_into.name())
                        .plate_number("")
                        .build();
//        cameraAlertService.createWorkOrderByAlert(deviceId,alertMessage);
    }

}
