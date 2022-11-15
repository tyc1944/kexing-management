package org.kexing.management.infrastruction.mapstruct;

import org.kexing.management.domin.model.sql_server.JZHuNeiDataUploadDate;
import org.kexing.management.domin.model.sql_server.JZHuWaiDataUploadDate;
import org.kexing.management.domin.model.sql_server.JZWeiKeNewDataUploadDate;
import org.kexing.management.domin.model.sql_server.JZWeiKeOldDataUploadDate;
import org.kexing.management.infrastruction.query.dto.sql_server.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * @author lh
 */
@Mapper(
        typeConversionPolicy = ReportingPolicy.ERROR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GetErpDeviceResponseMapping {
  GetErpHongXiangDeviceResponse createByGetErpDeviceResponse(
      GetErpDeviceResponse getErpDeviceResponse, Double currentTemperature);

  @Mapping(source = "deviceCraftMaterials",target="craftMaterials")
  GetErpHRDeviceResponse createByGetErpDeviceResponse(GetErpDeviceResponse getErpDeviceResponse, List<MeterialResponse> deviceCraftMaterials);

  @Mapping(source = "jzHuWaiDataUploadDate",target="jzHuWaiDataUploadDate")
  GetErpJZHuWaiDeviceResponse createByGetErpDeviceResponse(
          GetErpDeviceResponse getErpDeviceResponse, JZHuWaiDataUploadDate jzHuWaiDataUploadDate);

  @Mapping(source = "jzWeiKeNewDataUploadDate",target="jzWeiKeNewDataUploadDate")
  GetErpJZWeiKeNewDeviceResponse createByGetErpDeviceResponse(
          GetErpDeviceResponse getErpDeviceResponse, JZWeiKeNewDataUploadDate jzWeiKeNewDataUploadDate);


  @Mapping(source = "jzWeiKeOldDataUploadDate",target="jzWeiKeOldDataUploadDate")
  GetErpJZWeiKeOldDeviceResponse createByGetErpDeviceResponse(
          GetErpDeviceResponse getErpDeviceResponse, JZWeiKeOldDataUploadDate jzWeiKeOldDataUploadDate);

  @Mapping(source = "jzHuNeiDataUploadDate",target="jzHuNeiDataUploadDate")
  GetErpJZHuNeiDeviceResponse createByGetErpDeviceResponse(
          GetErpDeviceResponse getErpDeviceResponse, JZHuNeiDataUploadDate jzHuNeiDataUploadDate);
}
