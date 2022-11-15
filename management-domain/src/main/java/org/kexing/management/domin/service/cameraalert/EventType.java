package org.kexing.management.domin.service.cameraalert;

public enum EventType {
    people_in,//人员进入
    people_broke_into,//人员闯入
    not_wearing_overalls,//未穿戴工装
    people_gathered,//人员聚集
    long_absences_from_duty,//长时间离岗
    vehicle;//车辆经过

    public static String convert(String eventType){
        switch (EventType.valueOf(eventType)){
            case people_in:
                return "人员进入";
            case people_broke_into:
                return "人员闯入";
            case not_wearing_overalls:
                return "未穿戴工装";
            case people_gathered:
                return "人员聚集";
            case long_absences_from_duty:
                return "长时间离岗";
            case vehicle:
                return "车辆经过";
            default:return "";
        }
    }
}
