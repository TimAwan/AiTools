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
@TableName("consultation_record")
public class ConsultationRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 咨询编号
     */
    private String consultationNo;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 联系方式
     */
    private String contactInfo;

    /**
     * 使用场景
     */
    private String useCase;

    /**
     * 用户需求JSON
     */
    private String requirementsJson;

    /**
     * 推荐配置JSON
     */
    private String recommendedConfigJson;

    /**
     * 状态：待跟进、已联系、已成交、已关闭
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

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