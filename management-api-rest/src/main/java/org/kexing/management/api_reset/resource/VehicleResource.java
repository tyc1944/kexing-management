package org.kexing.management.api_reset.resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.kexing.management.domin.model.mysql.Vehicle;
import org.kexing.management.domin.repository.mysql.VehicleRepository;
import org.kexing.management.infrastruction.query.dto.VehicleRequest;
import org.kexing.management.infrastruction.query.dto.VehicleResponse;
import org.kexing.management.infrastruction.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Optional;

@RequestMapping("/api/vehicle")
@RestController
@Tag(name = "车辆")
@RequiredArgsConstructor
@Transactional
public class VehicleResource {

    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    VehicleService vehicleService;

    @PostMapping
    @Operation(description = "新建车辆")
    public void create(@RequestBody Vehicle vehicle){
        vehicle.setPlateNumber(vehicle.getPlateNumber().toUpperCase(Locale.ROOT));
        if(StringUtils.isBlank(vehicle.getPlateNumber())){
            throw new IllegalArgumentException("车牌号不能为空");
        }
        Vehicle vehicleDb = vehicleRepository.findByPlateNumber(vehicle.getPlateNumber());
        if(vehicleDb!=null){
            throw new IllegalArgumentException("此车牌号已存在");
        }
        if(vehicle.getStaffId()!=null){
            Vehicle vehicleDb1 = vehicleRepository.findByStaffId(vehicle.getStaffId());
            if(vehicleDb1!=null){
                throw new IllegalArgumentException("此员工已绑定车辆");
            }
        }
        vehicle.setId(null);
        vehicleRepository.save(vehicle);
    }

    @PutMapping("/{id}")
    @Operation(description = "编辑车辆")
    public void modify(@PathVariable long id,@RequestBody Vehicle vehicle){
        vehicle.setPlateNumber(vehicle.getPlateNumber().toUpperCase(Locale.ROOT));
        Optional<Vehicle> vehicleOperation = vehicleRepository.findById(id);
        if(vehicleOperation.isEmpty()){
            throw new IllegalArgumentException("此车辆不存在");
        }
        if(StringUtils.isBlank(vehicle.getPlateNumber())){
            throw new IllegalArgumentException("车牌号不能为空");
        }
        Vehicle vehiclePlateNumber = vehicleRepository.findByPlateNumber(vehicle.getPlateNumber());
        Vehicle vehicleDb = vehicleOperation.get();
        if(!vehicleDb.getPlateNumber().equals(vehicle.getPlateNumber())&&vehiclePlateNumber!=null){
            throw new IllegalArgumentException("此车牌号已存在");
        }
        vehicleDb.setPlateNumber(vehicle.getPlateNumber());
        vehicleDb.setStaffId(vehicle.getStaffId());
        vehicleRepository.save(vehicleDb);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        Optional<Vehicle> vehicleOperation = vehicleRepository.findById(id);
        if(vehicleOperation.isEmpty()){
            throw new IllegalArgumentException("此车辆不存在");
        }
        vehicleRepository.deleteById(id);
    }

    @GetMapping
    @Operation(description = "车辆列表")
    public IPage<VehicleResponse> list(@ModelAttribute VehicleRequest request){
        return vehicleService.list(request);
    }

    @GetMapping("/{id}")
    public VehicleResponse get(@PathVariable long id){
        VehicleResponse response = vehicleService.getById(id);
        if(response==null){
            throw new IllegalArgumentException("车辆不存在");
        }
        return response;
    }

}
