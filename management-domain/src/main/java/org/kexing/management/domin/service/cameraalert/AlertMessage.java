package org.kexing.management.domin.service.cameraalert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AlertMessage {
    private String event_type;
    private String emp_ids;
    private String emp_id;
    private String plate_number;
    private boolean boundary;
    private long start_time;
    private long end_time;
    private Long alert_time;
    private String alert_rtsp;
}
