package com.ght666.aiTools.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author 1012ght
 * 会话历史表
 */
@Data
@TableName("chat_history")
public class ChatHistory {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 业务类型，如：chat, pdf, service
     */
    private String type;

    /**
     * 会话ID
     */
    private String chatId;

    /**
     * 创建时间
     */
    private Date createdTime;
}