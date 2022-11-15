package org.kexing.management.infrastruction.repository.mybatis.sql_server;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.infrastruction.query.dto.sql_server.Material;
import org.kexing.management.infrastruction.query.dto.sql_server.ListMaterialRequest;


@Mapper
public interface MaterialQueryMapper {

    IPage<Material> selectListMaterialPage(
            @Param("page") Page<?> page,
            @Param("listMaterialRequest") ListMaterialRequest listMaterialRequest);
}
