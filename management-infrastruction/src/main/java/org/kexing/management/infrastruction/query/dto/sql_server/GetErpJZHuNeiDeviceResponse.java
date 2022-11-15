package org.kexing.management.infrastruction.query.dto.sql_server;

import lombok.Getter;
import lombok.Setter;
import org.kexing.management.domin.model.sql_server.JZHuNeiDataUploadDate;
import org.kexing.management.domin.model.sql_server.JZWeiKeNewDataUploadDate;

@Getter
@Setter
public class GetErpJZHuNeiDeviceResponse extends GetErpDeviceResponse {
    private JZHuNeiDataUploadDate jzHuNeiDataUploadDate;
}
