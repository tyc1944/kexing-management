package org.kexing.management.api_reset.resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.kexing.management.infrastruction.query.dto.sql_server.Material;
import org.kexing.management.infrastruction.mapstruct.PageParamMapper;
import org.kexing.management.infrastruction.query.BaseParam;
import org.kexing.management.infrastruction.query.dto.DeviceNameAndAttributesResponse;
import org.kexing.management.infrastruction.query.dto.sql_server.ListMaterialRequest;
import org.kexing.management.infrastruction.repository.mybatis.mysql.DeviceQueryMapper;
import org.kexing.management.infrastruction.repository.mybatis.sql_server.MaterialQueryMapper;
import org.kexing.management.infrastruction.service.DeviceViewService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
@Transactional
@Tag(name = "物料资源")
public class MaterialResource {
    private final MaterialQueryMapper materialQueryMapper;

    private final PageParamMapper paramMapper;

    private final DeviceViewService deviceViewService;

    private final DeviceQueryMapper deviceQueryMapper;
    @GetMapping
    @Operation(description = "物料列表")
    public IPage<Material> getAllMaterialPage(
            @Validated(value = {BaseParam.BaseParamVaGroup.class, Default.class}) @ModelAttribute
            ListMaterialRequest listMaterialRequest) {
        return materialQueryMapper.selectListMaterialPage(paramMapper.mapper(listMaterialRequest), listMaterialRequest);
    }

    @GetMapping("/camera") 
    @Operation(description = "物料对应的摄像头信息")
    public DeviceNameAndAttributesResponse getMaterialCamera() {
        return deviceViewService.getDeviceMaterialCamera(deviceQueryMapper.getDeviceListNotAttributes());
    }
}
