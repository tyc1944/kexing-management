package org.kexing.management.infrastruction.query.dto.sql_server;

import lombok.Getter;
import lombok.Setter;
import org.kexing.management.domin.model.sql_server.JZWeiKeOldDataUploadDate;

@Getter
@Setter
public class GetErpJZWeiKeOldDeviceResponse extends GetErpDeviceResponse{
    private JZWeiKeOldDataUploadDate jzWeiKeOldDataUploadDate;
}
