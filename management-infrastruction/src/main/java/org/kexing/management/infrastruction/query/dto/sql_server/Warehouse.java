package org.kexing.management.infrastruction.query.dto.sql_server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "仓库信息")
public class Warehouse {
    @Schema(description = "仓库编码 ")
    private String whsId;

    @Schema(description = "仓库名称")
    private String whsName;
}
