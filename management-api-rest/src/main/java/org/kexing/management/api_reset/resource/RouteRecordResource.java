package org.kexing.management.api_reset.resource;

import com.yunmo.domain.common.Linq;
import com.yunmo.iot.api.core.DeviceService;
import com.yunmo.iot.api.core.EntityEventService;
import com.yunmo.iot.domain.core.Device;
import com.yunmo.iot.domain.core.EntityEventRecordRpc;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.kexing.management.api_reset.dto.RouteRecordResponse;
import org.kexing.management.infrastruction.repository.mybatis.mysql.IotDeviceQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/routeRecord")
@RestController
@RequiredArgsConstructor
@Tag(name = "路线记录")
public class RouteRecordResource {

    @Autowired
    EntityEventService entityEventService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    IotDeviceQueryMapper iotDeviceQueryMapper;

    @Schema(description = "entity:员工轨迹staff_{empId},车辆轨迹:vehicle_{plateNumber}")
    @GetMapping("/{entity}/{day}")
    public List<?> list(@PathVariable String entity,
                        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day,
                        @RequestParam(required = false,defaultValue = "kexing_video_recorder") String channel){
        long time = day.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        List<EntityEventRecordRpc> entityEventRecordRpcs = entityEventService.queryTelemetryByDay(entity,channel,time);
        return Linq.leftJoin(entityEventRecordRpcs,"eventRecord",iotDeviceQueryMapper::selectByDeviceId,"device")
                .on(EntityEventRecordRpc::getDeviceId,Device::getId)
                .toList();

    }
}
