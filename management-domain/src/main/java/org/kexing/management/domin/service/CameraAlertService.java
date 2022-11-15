package org.kexing.management.domin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.yunmo.attendance.api.entity.Staff;
import com.yunmo.attendance.api.service.SchedulingRpcService;
import com.yunmo.attendance.api.service.StaffRpcService;
import com.yunmo.iot.api.core.DeviceService;
import com.yunmo.iot.domain.core.Device;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.Local;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Even;
import org.kexing.management.domin.model.mysql.AlertWorkOrderConfig;
import org.kexing.management.domin.model.mysql.DeviceSource;
import org.kexing.management.domin.model.mysql.UserAccount;
import org.kexing.management.domin.model.mysql.WorkOrder;
import org.kexing.management.domin.repository.mysql.AlertWorkOrderConfigRepository;
import org.kexing.management.domin.repository.mysql.StaffRepository;
import org.kexing.management.domin.repository.mysql.UserAccountRepository;
import org.kexing.management.domin.repository.mysql.WorkOrderRepository;
import org.kexing.management.domin.service.cameraalert.AlertMessage;
import org.kexing.management.domin.service.cameraalert.EventType;
import org.kexing.management.domin.util.DateUtil;
import org.kexing.management.domin.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class CameraAlertService {

    @Autowired
    WorkOrderRepository workOrderRepository;
    @Autowired
    WorkOrderService workOrderService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    DeviceService deviceService;
    @Autowired
    StaffRpcService staffRpcService;
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    SchedulingRpcService schedulingRpcService;
    @Autowired
    AlertWorkOrderConfigRepository alertWorkOrderConfigRepository;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     *
     * @param deviceId
     * @param
     */
    public void createWorkOrderByAlert(long deviceId, AlertMessage alertMessage) throws JsonProcessingException {
        boolean boundary = alertMessage.isBoundary();
        Device device = deviceService.getDeviceById(deviceId);
        if(boundary){
            if(EventType.people_broke_into.equals(EventType.valueOf(alertMessage.getEvent_type()))&&
                    !isPeopleBrokeIntoAllow(alertMessage.getEmp_ids())){
                createWorkOrder(device,alertMessage,"");
                return;
            }
            if(inWorkSectionTime(alertMessage.getAlert_time())&&
                    EventType.not_wearing_overalls.equals(EventType.valueOf(alertMessage.getEvent_type()))){
                createNotWearingOverallsWorkOrder(device,alertMessage);
                return;
            }
            if(EventType.people_gathered.equals(EventType.valueOf(alertMessage.getEvent_type()))||
                    EventType.long_absences_from_duty.equals(EventType.valueOf(alertMessage.getEvent_type()))){
                redisTemplate.opsForValue().set(getRedisKey(deviceId,alertMessage.getEvent_type()),getRedisValue(alertMessage));
                if(inCreateWorkOrderTime(alertMessage.getAlert_time())){
                    createWorkOrder(device,alertMessage,"");
                }
                return;
            }

        }
        if(!boundary){
            if(EventType.not_wearing_overalls.equals(EventType.valueOf(alertMessage.getEvent_type()))){
                if(StringUtils.isNotEmpty(alertMessage.getEmp_ids())){
                    for(String empId:alertMessage.getEmp_ids().split(",")){
                        List<WorkOrder> workOrders = workOrderRepository.findLastByDeviceIdAndEventTypeAndEmpId(deviceId,alertMessage.getEvent_type(),empId);
                        updateWorkOrder(workOrders,alertMessage);
                    }
                }else{
                    List<WorkOrder> workOrders = workOrderRepository.findLastByDeviceIdAndEventType(deviceId,alertMessage.getEvent_type());
                    updateWorkOrder(workOrders,alertMessage);
                }
                return;
            }
            if(EventType.people_broke_into.equals(EventType.valueOf(alertMessage.getEvent_type()))){
                List<WorkOrder> workOrders = workOrderRepository.findLastByDeviceIdAndEventType(deviceId,alertMessage.getEvent_type());
                updateWorkOrder(workOrders,alertMessage);
                return;
            }
            if(EventType.people_gathered.equals(EventType.valueOf(alertMessage.getEvent_type()))||
                    EventType.long_absences_from_duty.equals(EventType.valueOf(alertMessage.getEvent_type()))){
                Object values = redisTemplate.opsForValue().get(getRedisKey(deviceId,alertMessage.getEvent_type()));
                if(!Objects.isNull(values)){
                    AlertMessage message = objectMapper.readValue(values.toString(), AlertMessage.class);
                    if(inWorkSectionTime(message.getAlert_time())){
                        List<WorkOrder> workOrders = workOrderRepository.findLastByDeviceIdAndEventType(deviceId,alertMessage.getEvent_type());
                        updateWorkOrder(workOrders,alertMessage);
                    }
                }
                redisTemplate.opsForValue().set(getRedisKey(deviceId,alertMessage.getEvent_type()),getRedisValue(alertMessage));
            }
        }
    }

    private void updateWorkOrder(List<WorkOrder> workOrders,AlertMessage alertMessage){
        workOrders.forEach(workOrder -> {
            workOrder.getAttributes().put("end_time",alertMessage.getAlert_time());
            workOrder.getAttributes().put("boundary",alertMessage.isBoundary());
            workOrderRepository.save(workOrder);
        });
    }

    private void createNotWearingOverallsWorkOrder(Device device,AlertMessage alertMessage){
        LocalDate localDate = Instant.ofEpochSecond(alertMessage.getAlert_time()).atZone(ZoneId.systemDefault()).toLocalDate();
        if(StringUtils.isNotEmpty(alertMessage.getEmp_ids())){
            for(String empId:alertMessage.getEmp_ids().split(",")){
                if("unknown".equals(empId)){
                    createWorkOrder(device,alertMessage,empId);
                }else{
                    List<WorkOrder> workOrders = workOrderRepository.findByAlertTimeDay(localDate,empId,alertMessage.getEvent_type());
                    if(workOrders==null||workOrders.size()==0){
                        alertMessage.setEmp_id(empId);//用于分开多个员工同时未穿戴工装的工单
                        createWorkOrder(device,alertMessage,empId);
                    }
                }
            }
        }else{
            createWorkOrder(device,alertMessage,"");
        }

    }

    public String getRedisKey(Long deviceId,String eventType){
        return deviceId.toString().concat("_").concat(eventType);
    }

    public String getRedisValue(AlertMessage alertMessage){
        try {
            return objectMapper.writeValueAsString(alertMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return "";
    }

    public boolean inCreateWorkOrderTime(long alertTime){
        AlertWorkOrderConfig config = alertWorkOrderConfigRepository.findAll().get(0);
        LocalTime time = Instant.ofEpochSecond(alertTime).atZone(ZoneId.systemDefault()).toLocalTime();
        return (time.isAfter(config.getMorningStartWorkTime().plus(config.getTimeDistance(),ChronoUnit.MINUTES))&&time.isBefore(config.getMorningEndWorkTime()))
                ||(time.isAfter(config.getAfternoonStartWorkTime().plus(config.getTimeDistance(),ChronoUnit.MINUTES))&&time.isBefore(config.getAfternoonEndWorkTime()));
    }

    private boolean isPeopleBrokeIntoAllow(String empIds){
        if(StringUtils.isEmpty(empIds)){
            return false;
        }
        return Arrays.stream(empIds.split(",")).anyMatch(empId -> {
            Staff staff = staffRpcService.getByEmpId(empId);
            if(staff!=null){
                Optional<UserAccount> userAccountOptional = userAccountRepository.findByStaffId(staff.getId());
                return userAccountOptional.isPresent()&&userAccountOptional.get().isPeopleBrokeIntoAllow();
            }
            return false;
        });
    }

    public void createWorkOrder(Device device, AlertMessage alertMessage,String empId){
        alertMessage.setStart_time(alertMessage.getAlert_time());
        alertMessage.setAlert_rtsp(getRtsp(alertMessage.getAlert_time(),device));
        alertMessage.setEmp_id(empId);
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
        try {
            workOrderService.createWorkOrder1(
                    device.getId(),
                    device.getAttributes().get("location").toString(),
                    getProblemDescription(StringUtils.isEmpty(empId)?alertMessage.getEmp_ids():empId,alertMessage),
                    WorkOrder.Type.EVENT,
                    objectMapper.readValue(objectMapper.writeValueAsString(alertMessage),mapType),
                    DeviceSource.IOT);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    public String getRtsp(long alertTime,Device device){
        Instant eventTime = Instant.ofEpochSecond(alertTime);
        Instant startDate = eventTime.minus(2, ChronoUnit.MINUTES);
        Instant endDate = eventTime.plus(2, ChronoUnit.MINUTES);
        return String.format(device.getAttributes().get("video_recoder_rtsp").toString()+"?starttime=%s&endtime=%s",
                StringUtil.convertInstant(startDate),
                StringUtil.convertInstant(endDate));
    }

    private String getProblemDescription(String empIds,AlertMessage alertMessage){
        Instant eventTime = Instant.ofEpochSecond(alertMessage.getAlert_time());
        String problemDescription = "";
        if(StringUtils.isNotEmpty(empIds)){
            for(String empId:empIds.split(",")){
                Staff staff = staffRpcService.getByEmpId(empId);
                if(staff!=null){
                    problemDescription = problemDescription
                            .concat(DateUtil.convert2String(eventTime,DateUtil.YYY_MM_DD))
                            .concat(" ")
                            .concat(DateUtil.convert2String(eventTime,DateUtil.HH_MM))
                            .concat(" ")
                            .concat(staff.getName()).concat("（工号：").concat(empId).concat("）")
                            .concat(" ")
                            .concat(EventType.convert(alertMessage.getEvent_type()))
                            .concat(";");
                }else{
                    problemDescription = problemDescription.concat(EventType.convert(alertMessage.getEvent_type()));
                }
            }
        }else{
            problemDescription = problemDescription.concat(EventType.convert(alertMessage.getEvent_type()));
        }
        return problemDescription;
    }

    public boolean inWorkSectionTime(long alertTime){
        return isInAlertTimeSection(Instant.ofEpochSecond(alertTime))
                &&schedulingRpcService.isWorkingDay(Instant.now());
    }


    /**
     * 8:00-11:00  13:00-17:00
     * @param time
     * @return
     */
    private boolean isInAlertTimeSection(Instant time){
        AlertWorkOrderConfig config = alertWorkOrderConfigRepository.findAll().get(0);
        LocalTime alertTime = time.atZone(ZoneId.systemDefault()).toLocalTime();
        return (alertTime.compareTo(config.getMorningStartWorkTime())>=0&&alertTime.compareTo(config.getMorningEndWorkTime())<=0)||
                (alertTime.compareTo(config.getAfternoonStartWorkTime())>=0&&alertTime.compareTo(config.getAfternoonEndWorkTime())<=0);
    }
}
