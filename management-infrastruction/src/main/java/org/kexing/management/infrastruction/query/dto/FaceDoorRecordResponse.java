package org.kexing.management.infrastruction.query.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Data
public class FaceDoorRecordResponse {

    @ExcelIgnore
    private Long id;
    @ExcelIgnore
    private Long staffId;
    @ExcelIgnore
    private Long deviceId;
    @ExcelProperty(value = "姓名")
    @Schema(description = "员工姓名")
    private String staffName;
    @ExcelProperty(value = "工号")
    @Schema(description = "员工工号")
    private String empId;
    @ExcelProperty(value = "部门")
    @Schema(description = "部门")
    private String organizationName;
    @ExcelProperty(value = "体温")
    @Schema(description = "人脸温度")
    private Float faceTemperature;
    @ExcelIgnore
    @Schema(description = "位置")
    private String location;
    @ExcelProperty(value = "日期")
    private String date;
    @ExcelProperty(value = "进出时间")
    private String time;
    @ExcelIgnore
    @Schema(description = "测温单位（0-摄氏度（默认），1-华氏度，2-开尔文）")
    private Integer temperatureUnit;
    @ExcelIgnore
    @Schema(description = "人脸温度坐标x")
    private Float regionCoordinatesx;
    @ExcelIgnore
    private Float regionCoordinatesy;
    @ExcelIgnore
    @Schema(description = "人脸图片(base64)")
    private String pic;//base64
    @ExcelIgnore
    @Schema(description = "人脸抓拍测温是否温度异常：1-是，0-否")
    private Boolean abnormalTemperature;
    @ExcelIgnore
    private Instant createdDate;
    @ExcelIgnore
    private Long organizationId;

}
