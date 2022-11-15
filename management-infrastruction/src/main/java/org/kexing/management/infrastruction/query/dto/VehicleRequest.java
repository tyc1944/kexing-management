package org.kexing.management.infrastruction.query.dto;


import lombok.Data;
import org.kexing.management.infrastruction.query.BaseParam;

@Data
public class VehicleRequest  extends BaseParam {
    private String organizationId;
}
