package com.example.testcloudprovider.enums;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 导出任务类型枚举
 */
public enum ExportJobSourceEnum {

    ORDER_JOB("order","订单查询导出"),
    INCOME_JOB("income","收入确认"),
    TUIGAI_JOB("tuigai","退改"),
    PERFQUERY_JOB("perfQuery","业绩查询"),
    PERFSTAT_JOB("perfStat","业绩报表");

    private String value;
    private String desc;

    ExportJobSourceEnum(String value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByValue(String value){
        return Stream.of(ExportJobSourceEnum.values()).filter(s ->
                Objects.equals(value, s.getValue())).map(o -> o.getDesc()).findFirst().get();
    }
}
