package org.kexing.management.infrastruction.query.dto.sql_server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lh
 */
@Schema(description = "工艺物料")
@Setter
@Getter
public class MeterialResponse {
    @Schema(description = "物料id")
    private String materialId;

    @Schema(description = "物料名称")
    private String materialName;

    @Schema(description = "物料规格")
    private String materialSpec;
}
