package org.kexing.management.infrastruction.mapstruct;

import org.kexing.management.domin.model.mysql.ErpDeviceConfig;
import org.kexing.management.infrastruction.query.dto.sql_server.ListErpDeviceResponse;
import org.kexing.management.infrastruction.query.dto.sql_server.ListErpHongXiangDeviceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


/** @author lh */
@Mapper(
    typeConversionPolicy = ReportingPolicy.ERROR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ListErpDeviceResponseMapping {

  ListErpHongXiangDeviceResponse createByListErpDeviceResponse(
          ListErpDeviceResponse listErpDeviceResponse, ErpDeviceConfig.HongXiangDeviceConfig hongXiangDeviceConfig, Double currentTemperature);
}
