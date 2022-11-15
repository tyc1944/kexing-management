package org.kexing.management.infrastruction.repository.mybatis.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunmo.iot.domain.core.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IotDeviceQueryMapper extends BaseMapper<Device> {

    Device selectByDeviceId(@Param("id") long id);
}
