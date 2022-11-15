package org.kexing.management.infrastruction.repository.mybatis.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.domin.model.mysql.WorkOrder;
import org.kexing.management.infrastruction.query.dto.VehicleRequest;
import org.kexing.management.infrastruction.query.dto.VehicleResponse;

public interface VehicleQueryMapper extends BaseMapper<VehicleResponse> {
    IPage<VehicleResponse> listVehicles(@Param("page") Page page, @Param("vehicleRequest") VehicleRequest request);

    VehicleResponse selectById(@Param("id") long id);
}
