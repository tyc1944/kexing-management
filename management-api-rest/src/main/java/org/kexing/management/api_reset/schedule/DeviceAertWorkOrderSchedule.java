package org.kexing.management.api_reset.schedule;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunmo.iot.api.core.DeviceService;
import com.yunmo.iot.domain.core.Device;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.Local;
import org.apache.commons.lang3.StringUtils;
import org.kexing.management.domin.model.mysql.AlertWorkOrderConfig;
import org.kexing.management.domin.repository.mysql.AlertWorkOrderConfigRepository;
import org.kexing.management.domin.repository.mysql.IotErpDeviceBindMapRepository;
import org.kexing.management.domin.service.CameraAlertService;
import org.kexing.management.domin.service.cameraalert.AlertMessage;
import org.kexing.management.domin.service.cameraalert.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DeviceAertWorkOrderSchedule {

    @Autowired
    CameraAlertService cameraAlertService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    DeviceService deviceService;
    @Autowired
    AlertWorkOrderConfigRepository alertWorkOrderConfigRepository;
    @Autowired
    IotErpDeviceBindMapRepository iotErpDeviceBindMapRepository;

    @Scheduled(cron = "0 */1 * * * ?")
    public void task(){
        if(inCreateWorkOrderTime()){
            List<Device> deviceList = deviceService.findByAttributesProperty("deviceType","camera")
                    .stream()
                    .filter(d->!d.isDeleted())
                    .filter(d->iotErpDeviceBindMapRepository.findByIotDeviceId(d.getId()).size()>0)
                    .collect(Collectors.toList());
            deviceList.forEach(device -> {
                createWorkOrderByEventType(device,EventType.long_absences_from_duty);
                createWorkOrderByEventType(device,EventType.people_gathered);
            });
        }
    }

    private boolean inCreateWorkOrderTime(){
        AlertWorkOrderConfig config = alertWorkOrderConfigRepository.findAll().get(0);
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        LocalTime morningStart = config.getMorningStartWorkTime().plus(config.getTimeDistance(),ChronoUnit.MINUTES);
        LocalTime afternoonStart = config.getAfternoonStartWorkTime().plus(config.getTimeDistance(),ChronoUnit.MINUTES);
        return (now.compareTo(morningStart)==0)
                ||(now.compareTo(afternoonStart)==0);
    }

    private void createWorkOrderByEventType(Device device,EventType eventType){
        Object values = redisTemplate.opsForValue()
                .get(
                        cameraAlertService.getRedisKey(device.getId(), eventType.name())
                );
        if(values==null){
            return;
        }
        try {
            AlertMessage message = objectMapper.readValue(values.toString(), AlertMessage.class);
            if(message.isBoundary()){
                message.setAlert_time(Instant.now().getEpochSecond());
                message.setBoundary(false);
                cameraAlertService.createWorkOrder(device,message,"");
                redisTemplate.opsForValue().set(cameraAlertService.getRedisKey(device.getId(),message.getEvent_type()),cameraAlertService.getRedisValue(message));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }


}
