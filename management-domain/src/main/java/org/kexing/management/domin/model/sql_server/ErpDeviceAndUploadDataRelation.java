package org.kexing.management.domin.model.sql_server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * @author lh
 */
@Data
public class ErpDeviceAndUploadDataRelation {
    private String deviceId;
    @Schema(description = "身份标识")
    private String tagUID;
}
