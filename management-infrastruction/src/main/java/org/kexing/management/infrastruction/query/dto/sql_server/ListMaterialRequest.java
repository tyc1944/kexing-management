package org.kexing.management.infrastruction.query.dto.sql_server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.kexing.management.infrastruction.query.BaseParam;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Setter
@Getter
@Schema(description = "物料记录列表查询参数")
public class ListMaterialRequest extends BaseParam {

    @Schema(description = "入库时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Size(min = 2, max = 2)
    private LocalDate[] dateRange;

    @Schema(description = "仓库Id")
    private Long warehouseId;

}
