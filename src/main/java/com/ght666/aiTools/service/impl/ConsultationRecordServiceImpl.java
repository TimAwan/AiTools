package com.ght666.aiTools.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ght666.aiTools.entity.po.ConsultationRecord;
import com.ght666.aiTools.mapper.ConsultationRecordMapper;
import com.ght666.aiTools.service.IConsultationRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 咨询记录服务实现类
 */
@Service
public class ConsultationRecordServiceImpl extends ServiceImpl<ConsultationRecordMapper, ConsultationRecord> implements IConsultationRecordService {
    
    @Override
    public ConsultationRecord findByConsultationNo(String consultationNo) {
        return this.query()
                .eq("consultation_no", consultationNo)
                .one();
    }
    
    @Override
    public List<ConsultationRecord> findByStatus(String status) {
        return this.query()
                .eq("status", status)
                .orderByDesc("created_time")
                .list();
    }
    
    @Override
    public List<ConsultationRecord> findByCustomerName(String customerName) {
        return this.query()
                .like("customer_name", customerName)
                .orderByDesc("created_time")
                .list();
    }
    
    @Override
    public List<ConsultationRecord> findByContactInfo(String contactInfo) {
        return this.query()
                .eq("contact_info", contactInfo)
                .orderByDesc("created_time")
                .list();
    }
    
    @Override
    public boolean updateStatus(String consultationNo, String status) {
        ConsultationRecord record = findByConsultationNo(consultationNo);
        if (record != null) {
            record.setStatus(status);
            record.setUpdatedTime(LocalDateTime.now());
            return this.updateById(record);
        }
        return false;
    }
    
    @Override
    public boolean updateRecommendedConfig(String consultationNo, String recommendedConfigJson) {
        ConsultationRecord record = findByConsultationNo(consultationNo);
        if (record != null) {
            record.setRecommendedConfigJson(recommendedConfigJson);
            record.setUpdatedTime(LocalDateTime.now());
            return this.updateById(record);
        }
        return false;
    }
}