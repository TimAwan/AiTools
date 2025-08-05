// src/main/java/com/ght666/aiTools/mapper/ChatMessageMapper.java
package com.ght666.aiTools.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ght666.aiTools.entity.po.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
    int insertBatchSomeColumn(@Param("list") List<ChatMessage> list);
}