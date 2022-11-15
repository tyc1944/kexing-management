package org.kexing.management.api_reset.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.kexing.management.infrastruction.query.dto.sql_server.Warehouse;
import org.kexing.management.infrastruction.repository.mybatis.sql_server.WarehouseQueryMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
@RequiredArgsConstructor
@Transactional
@Tag(name = "仓库")
public class WarehouseResource {
    private final WarehouseQueryMapper warehouseQueryMapper;

    @GetMapping
    @Operation(description = "仓库全部列表")
    public List<Warehouse> geAll(){
        return warehouseQueryMapper.selectWarehouseList();
    }
}
