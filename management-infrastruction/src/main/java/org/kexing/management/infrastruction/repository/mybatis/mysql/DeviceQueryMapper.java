package org.kexing.management.infrastruction.repository.mybatis.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunmo.iot.domain.core.Device;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.domin.model.mysql.WorkOrder;
import org.kexing.management.infrastruction.query.dto.DeviceNameAndAttributesResponse;
import org.kexing.management.infrastruction.query.dto.IotDeviceWrap;
import org.kexing.management.infrastruction.query.dto.ListDeviceRequest;
import org.kexing.management.infrastruction.query.dto.statistics.IotDeviceStatusStatisticsResponse;

import java.util.List;

public interface DeviceQueryMapper extends BaseMapper<Device> {

  IPage<IotDeviceWrap> selectListDeviceRequestPage(
      @Param("page") Page<?> page, @Param("listDeviceRequest") ListDeviceRequest listDeviceRequest);

  List<DeviceNameAndAttributesResponse> selectAllDevice();

  DeviceNameAndAttributesResponse selectByDeviceId(@Param("deviceId") Long deviceId);

    IotDeviceStatusStatisticsResponse selectOnlineAndOfflineStatisticsByDeviceType(@Param("sourceType") WorkOrder.SourceType sourceType);

    Device getParentByCameraDeviceId(@Param("id") long id, @Param("deviceType") String deviceType);

    List<DeviceNameAndAttributesResponse> getDeviceListNotAttributes();
}
