package org.kexing.management.api_reset.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kexing.management.domin.model.mysql.WorkOrder;

import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HaikangDevice {
    @ExcelProperty
    private String name;
    @ExcelProperty
    private String ip;
    @ExcelProperty
    private Integer port;
    @ExcelProperty
    private String type;
    @ExcelProperty
    private String location;
    @ExcelProperty
    private String username;
    @ExcelProperty
    private String password;
    @ExcelProperty
    private String workshopDeviceMacIds;//多个用英文逗号隔开
    @ExcelProperty
    private String gatewayBindIps;//多个用英文逗号隔开
    @ExcelProperty
    private String videoRecorderBindCameras;//录像机绑定的摄像头 ip1:channel1,ip2:channel2
    @ExcelProperty
    private String doorType;//in  out
    @ExcelProperty
    private String deviceNo;
    @ExcelProperty
    private String cameraType;//枪机摄像头-bolt  广角摄像头-wide_angle
    @ExcelProperty
    private String deviceSpec;

}
