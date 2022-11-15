package org.kexing.management.infrastruction.repository.mybatis.sql_server;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.infrastruction.query.dto.ErpDeviceResponse;

import java.util.List;

/** @author lh */
public interface MacQueryMapper extends BaseMapper {
  List<ErpDeviceResponse> selectMacs();

  ErpDeviceResponse selectByMacId(@Param("macId") String macId);

  List<ErpDeviceResponse> selectByMacNames(@Param("macNames") List<String> macNames);
}
