package org.kexing.management.infrastruction.service.sql_server;

import lombok.RequiredArgsConstructor;
import org.kexing.management.domin.model.sql_server.ErpDeviceAndUploadDataRelation;
import org.kexing.management.domin.model.sql_server.ErpDeviceUploadVal;
import org.kexing.management.domin.model.sql_server.TagUIDUploadData;
import org.kexing.management.domin.service.ErpDeviceRunStatusConfigService;
import org.kexing.management.domin.util.ErpDeviceUtil;
import org.kexing.management.infrastruction.repository.mybatis.sql_server.ErpDeviceAndUploadDataRelationQueryMapper;
import org.kexing.management.infrastruction.repository.mybatis.sql_server.ErpTagUIDUploadDataQueryMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** @author lh */
@Service
@RequiredArgsConstructor
public class HongXiangViewService {
  private final ErpDeviceAndUploadDataRelationQueryMapper erpDeviceAndUploadDataRelationQueryMapper;
  private final ErpTagUIDUploadDataQueryMapper erpTagUIDUploadDataQueryMapper;

  public Optional<Double> getHongXiangCurrentTemperature(String hongXiangDeviceId) {
    Optional<ErpDeviceUploadVal> erpDeviceUploadVal =
        getHongXiangCurrentTemperatureAndUploadDate(hongXiangDeviceId);
    return erpDeviceUploadVal.map(item -> item.getUploadDate().doubleValue());
  }

  public Optional<ErpDeviceUploadVal> getHongXiangCurrentTemperatureAndUploadDate(String hongXiangDeviceId){
    ErpDeviceAndUploadDataRelation erpDeviceAndUploadDataRelation =
            erpDeviceAndUploadDataRelationQueryMapper.selectErpDeviceAndUploadDataRelation(
                    hongXiangDeviceId);
    if (erpDeviceAndUploadDataRelation == null) {
      return Optional.empty();
    }

    TagUIDUploadData tagUIDUploadData =
            erpTagUIDUploadDataQueryMapper.selectTagUIDUploadData(
                    erpDeviceAndUploadDataRelation.getTagUID());
    if (tagUIDUploadData == null) {
      return Optional.empty();
    }
    BigDecimal currentUploadVal =  ErpDeviceRunStatusConfigService.isValidUploadDate(tagUIDUploadData.getUpdateTime()) ? tagUIDUploadData.getUploadNumber() : null;
    if(currentUploadVal==null){
    return Optional.empty();
    }
    return  Optional.of( ErpDeviceUploadVal.builder().deviceId(hongXiangDeviceId).updateTime(tagUIDUploadData.getUpdateTime()).uploadDate(currentUploadVal).build());
  }

  public List<String>  getAllHongXiangBelongGroupBy(String hongXiangDeviceId){
    ErpDeviceAndUploadDataRelation erpDeviceAndUploadDataRelation =
        erpDeviceAndUploadDataRelationQueryMapper.selectErpDeviceAndUploadDataRelation(
            hongXiangDeviceId);
    if (erpDeviceAndUploadDataRelation==null) {
      return new ArrayList<>();
    }
    return erpDeviceAndUploadDataRelationQueryMapper.selectErpDeviceAndUploadDataRelationByTagUIDAndDeviceIdLike(erpDeviceAndUploadDataRelation.getTagUID(), ErpDeviceUtil.HONGXIANG_ID_PRE)
            .stream()
            .map(ErpDeviceAndUploadDataRelation::getDeviceId)
            .collect(Collectors.toList());

  }

}
