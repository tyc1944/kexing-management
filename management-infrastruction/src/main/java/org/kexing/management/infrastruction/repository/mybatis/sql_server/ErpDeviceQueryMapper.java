package org.kexing.management.infrastruction.repository.mybatis.sql_server;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.infrastruction.query.dto.sql_server.*;

import java.util.List;
import java.util.Map;

public interface ErpDeviceQueryMapper extends BaseMapper {
  IPage<ListErpDeviceResponse> selectListErpDevice(
          @Param("page") Page page,
          @Param("queryMacNameList") List<String> queryMacNameList,
          @Param("hxDeviceIdNotContain") List<String> hxDeviceIdNotContain,
          @Param("listErpDeviceRequest") ListErpDeviceRequest listErpDeviceRequest);

  List<ListErpDeviceResponse> selectListErpDevice(
          @Param("queryMacNameList") List<String> queryMacNameList,
          @Param("hxDeviceIdNotContain") List<String> hxDeviceIdNotContain,
          @Param("listErpDeviceRequest") ListErpDeviceRequest listErpDeviceRequest);

  GetErpDeviceResponse selectErpDevice(@Param("deviceId") String deviceId);

  IPage<ListErpDeviceProductRecordResponse> selectListErpDeviceProductRecord(
      @Param("page") Page page,
      @Param("deviceId") String deviceId,
      @Param("listDeviceProductRecordRequest")
          ListErpDeviceProductRecordRequest listDeviceProductRecordRequest);


  List<MeterialResponse> selectCraftMaterial(@Param("productNumber") String productNumber, @Param("prcId") String prcId);
}
