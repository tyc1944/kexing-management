package org.kexing.management.infrastruction.query.dto.sql_server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import java.time.Instant;

@Data
@Schema(description = "仓库信息")
public class Material {
    @Schema(description = "批号")
    private String batchNum;
    @Schema(description = "库存数量")
    private Double onHand;
    @Schema(description = "预约量")
    private Double onOccupy;
    @Schema(description = "生产日期")
    private Instant inDate;
    @Schema(description = "失效日期")
    private Instant invalidDate;
    @Schema(description = "最后入库")
    private Instant lastDateIn;
    @Schema(description = "最后出库")
    private Instant lastDateOut;
    @Schema(description = "仓库名")
    private String whsName;
    @Schema(description = "物料编号")
    private String itmID;
    @Schema(description = "物料名称")
    private String itmName;
    @Schema(description = "规格")
    private String itmSpec;

}
