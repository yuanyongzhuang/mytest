package com.example.testcloudprovider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yyz
 * @since 2021-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ExportJob implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 导出创建人
     */
    private Integer createUserGroupId;

    /**
     * order-订单查询导出,income-收入确认,tuigai-退改,perfQuery-业绩查询,perfStat-业绩报表
     */
    private String exportSource;

    /**
     * 下载地址uri
     */
    private String fileUri;

    /**
     * 文件名称
     */
    @TableField("fileName")
    private String fileName;

    /**
     * 进度，0未开始
     */
    private Integer process;

    /**
     * 导出条件
     */
    private String exportClause;


}
