package org.kexing.management.infrastruction.query.dto;

import lombok.Data;

@Data
public class VehicleResponse {

    private long id;
    private Long staffId;
    private String plateNumber;
    private String name;
    private String empId;
    private String phone;
    private String organizationName;
}
