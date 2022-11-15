package org.kexing.management.infrastruction.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.kexing.management.infrastruction.mapstruct.PageParamMapper;
import org.kexing.management.infrastruction.query.dto.VehicleRequest;
import org.kexing.management.infrastruction.query.dto.VehicleResponse;
import org.kexing.management.infrastruction.repository.mybatis.mysql.VehicleQueryMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final PageParamMapper pageParamMapper;
    private final VehicleQueryMapper vehicleQueryMapper;

    public IPage<VehicleResponse> list(VehicleRequest request){
        Page page = pageParamMapper.mapper(request);
        return vehicleQueryMapper.listVehicles(page,request);
    }

    public VehicleResponse getById(long id){
        return vehicleQueryMapper.selectById(id);
    }
}
