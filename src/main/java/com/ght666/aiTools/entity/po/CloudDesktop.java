package com.ght666.aiTools.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cloud_desktop")
public class CloudDesktop implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 配置名称
     */
    private String name;

    /**
     * 配置类型：经济型、标准型、专业型、高端型
     */
    private String type;

    /**
     * 配置详情JSON
     */
    private String configJson;

    /**
     * 状态(1:可用,0:不可用)
     */
    @TableLogic(value = "1", delval = "0")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}