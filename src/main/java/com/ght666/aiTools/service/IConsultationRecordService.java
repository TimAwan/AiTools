package com.ght666.aiTools.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ght666.aiTools.entity.po.ConsultationRecord;

import java.util.List;

/**
 * 咨询记录服务接口
 */
public interface IConsultationRecordService extends IService<ConsultationRecord> {
    
    /**
     * 根据咨询编号查询
     */
    ConsultationRecord findByConsultationNo(String consultationNo);
    
    /**
     * 根据状态查询
     */
    List<ConsultationRecord> findByStatus(String status);
    
    /**
     * 根据客户姓名查询
     */
    List<ConsultationRecord> findByCustomerName(String customerName);
    
    /**
     * 根据联系方式查询
     */
    List<ConsultationRecord> findByContactInfo(String contactInfo);
    
    /**
     * 更新咨询状态
     */
    boolean updateStatus(String consultationNo, String status);
    
    /**
     * 更新推荐配置
     */
    boolean updateRecommendedConfig(String consultationNo, String recommendedConfigJson);
}