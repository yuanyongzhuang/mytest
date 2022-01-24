package com.example.testcloudprovider.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * poi
 */
@Data
@Accessors(chain = true)
public class PoiModel {
    //内容
    private String content;
    //上一行同一位置内容
    private String oldContent;
    //行标
    private int rowIndex;
    //列标
    private int cellIndex;
}
