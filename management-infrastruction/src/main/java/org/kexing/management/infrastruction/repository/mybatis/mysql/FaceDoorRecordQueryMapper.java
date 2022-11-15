package org.kexing.management.infrastruction.repository.mybatis.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunmo.iot.domain.core.Device;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.infrastruction.query.dto.FaceDoorRecordResponse;
import org.kexing.management.infrastruction.query.dto.ListDeviceRequest;
import org.kexing.management.infrastruction.query.dto.ListFaceDoorRecordRequest;

public interface FaceDoorRecordQueryMapper extends BaseMapper<FaceDoorRecordResponse> {

    IPage<FaceDoorRecordResponse> selectListFaceDoorRecord(
            @Param("page") Page<?> page,
            @Param("listFaceDoorRecordRequest") ListFaceDoorRecordRequest listFaceDoorRecordRequest
    );
}
