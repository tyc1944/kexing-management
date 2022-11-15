/**
  删除人脸机和道闸数据
 */
delete FROM `iot-core`.asset_entity where device_id in (
    select id from `iot-core`.device where product_id in (304757135445590017,309468817648517121)
);
delete from `iot-core`.device where product_id in (304757135445590017,309468817648517121);
delete FROM `kexing_management`.iot_erp_device_bind_map;
delete from `iot-core`.device_hub where product_id in (304757135445590017,309468817648517121);
delete from `iot-core`.message_schema  where product_id in (304757135445590017,309468817648517121);
delete from `iot-core`.product where id in (304757135445590017,309468817648517121);

/**
  删除温湿度传感器，测温传感器，摄像头，网关，录像机数据
 */
delete FROM `iot-core`.asset_entity where device_id in (
    select id from `iot-core`.device where product_id in (319468817648517121,329468817648517121,339468817648517121,349468817648517121,359468817648517121)
);
delete from `iot-core`.device where product_id in (319468817648517121,329468817648517121,339468817648517121,349468817648517121,359468817648517121);
delete FROM `kexing_management`.iot_erp_device_bind_map;
delete from `iot-core`.device_hub where product_id in (319468817648517121,329468817648517121,339468817648517121,349468817648517121,359468817648517121);
delete from `iot-core`.message_schema  where product_id in (319468817648517121,329468817648517121,339468817648517121,349468817648517121,359468817648517121);
delete from `iot-core`.product where id in (319468817648517121,329468817648517121,339468817648517121,349468817648517121,359468817648517121);
delete from `iot-core`.analysis_table where id in (114929542181421058);
delete from `iot-core`.`rule_spec` where id in (319208841403170819);