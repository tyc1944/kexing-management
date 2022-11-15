package org.kexing.management.infrastruction.repository.mybatis.sql_server;

import org.apache.ibatis.annotations.Mapper;
import org.kexing.management.infrastruction.query.dto.sql_server.Warehouse;

import java.util.List;

@Mapper
public interface WarehouseQueryMapper {
    List<Warehouse> selectWarehouseList();
}
