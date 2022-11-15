package org.kexing.management.infrastruction.query.dto.sql_server;

import lombok.Getter;
import lombok.Setter;
import org.kexing.management.domin.model.sql_server.JZHuWaiDataUploadDate;

@Getter
@Setter
public class GetErpJZHuWaiDeviceResponse extends GetErpDeviceResponse{

    private JZHuWaiDataUploadDate jzHuWaiDataUploadDate;
}
