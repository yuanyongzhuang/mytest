package com.example.testcloudprovider.enums;

import java.util.Objects;
import java.util.stream.Stream;

public enum ExportJobProvessEnum {
    STARTED(0,"未开始"),
    SCHEDULE_1(1,"生成中(10%)"),
    SCHEDULE_2(2,"生成中(20%)"),
    SCHEDULE_3(3,"生成中(30%)"),
    SCHEDULE_4(4,"生成中(40%)"),
    SCHEDULE_5(5,"生成中(50%)"),
    SCHEDULE_6(6,"生成中(60%)"),
    SCHEDULE_7(7,"生成中(70%)"),
    SCHEDULE_8(8,"生成中(80%)"),
    SCHEDULE_9(9,"生成中(90%)"),
    OVER(10,"已生成"),
    FAIL(-1,"生成失败");

    private Integer value;
    private String desc;

    ExportJobProvessEnum(Integer value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue(){
        return value;
    }

    public String getDesc(){
        return desc;
    }

    public static String getDescByValue(Integer value){
        return Stream.of(ExportJobProvessEnum.values()).filter(s ->
                Objects.equals(value, s.getValue())).map(o -> o.getDesc()).findFirst().get();
    }
}
