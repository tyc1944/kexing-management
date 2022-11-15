package org.kexing.management.infrastruction.repository.mybatis.sql_server;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.domin.model.sql_server.ErpDeviceAndUploadDataRelation;

import java.util.List;

/**
 * @author lh
 */
public interface ErpDeviceAndUploadDataRelationQueryMapper extends BaseMapper {
  ErpDeviceAndUploadDataRelation selectErpDeviceAndUploadDataRelation(@Param("deviceId") String deviceId);

  List<ErpDeviceAndUploadDataRelation> selectErpDeviceAndUploadDataRelationByTagUIDAndDeviceIdLike(
          @Param("tagUID") String tagUID, @Param("deviceIdLike") String deviceIdLike);
}
