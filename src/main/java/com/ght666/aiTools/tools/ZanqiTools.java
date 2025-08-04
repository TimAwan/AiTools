package com.ght666.aiTools.tools;

import com.ght666.aiTools.entity.po.CloudDesktop;
import com.ght666.aiTools.entity.po.ConsultationRecord;
import com.ght666.aiTools.service.ICloudDesktopService;
import com.ght666.aiTools.service.IConsultationRecordService;
import com.ght666.aiTools.utils.RequirementsJsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author 1012ght
 */
@RequiredArgsConstructor
@Component
public class ZanqiTools {

    private final ICloudDesktopService cloudDesktopService;
    private final IConsultationRecordService consultationRecordService;
    private final RequirementsJsonUtil requirementsJsonUtil;

    @Tool(description = "查询所有云桌面配置")
    public List<CloudDesktop> queryAllCloudDesktop() {
        return cloudDesktopService.list();
    }

    @Tool(description = "根据类型查询云桌面配置")
    public List<CloudDesktop> queryCloudDesktopByType(
            @ToolParam(description = "配置类型") String type) {
        return cloudDesktopService.query()
                .eq("type", type)
                .list();
    }

    @Tool(description = "记录咨询信息,并返回咨询编号")
    public String recordConsultation(
            String customerName, String contactInfo, String useCase, 
            String requirementsJson, String specialRequirements) {
        
        ConsultationRecord record = new ConsultationRecord();
        record.setConsultationNo("ZQ" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8));
        record.setCustomerName(customerName);
        record.setContactInfo(contactInfo);
        record.setUseCase(useCase);
        record.setRequirementsJson(requirementsJson);
        //record.setSpecialRequirements(specialRequirements);
        record.setStatus("待跟进");
        record.setCreatedTime(LocalDateTime.now());
        record.setUpdatedTime(LocalDateTime.now());
        
        consultationRecordService.save(record);
        return record.getConsultationNo();
    }

    @Tool(description = "更新推荐配置")
    public String updateRecommendedConfig(
            @ToolParam(description = "咨询编号") String consultationNo,
            @ToolParam(description = "推荐配置JSON") String recommendedConfigJson) {
        
        ConsultationRecord record = consultationRecordService.findByConsultationNo(consultationNo);
        if (record != null) {
            record.setRecommendedConfigJson(recommendedConfigJson);
            record.setUpdatedTime(LocalDateTime.now());
            consultationRecordService.updateById(record);
            return "推荐配置已更新";
        }
        return "未找到该咨询记录";
    }

    @Tool(description = "查询咨询记录状态")
    public String queryConsultationStatus(@ToolParam(description = "咨询编号") String consultationNo) {
        ConsultationRecord record = consultationRecordService.findByConsultationNo(consultationNo);
        
        if (record != null) {
            return "咨询编号：" + record.getConsultationNo() + 
                   "，客户：" + record.getCustomerName() + 
                   "，状态：" + record.getStatus() + 
                   "，我们会尽快安排专业顾问联系您";
        }
        return "未找到该咨询记录";
    }

    @Tool(description = "获取销售顾问联系方式")
    public String getSalesContact() {
        return "销售顾问联系方式：\n" +
               "电话：400-888-8888\n" +
               "微信：zanqi-sales\n" +
               "邮箱：sales@zanqi.com\n" +
               "工作时间：周一至周五 9:00-18:00";
    }

    @Tool(description = "获取技术支持联系方式")
    public String getTechSupportContact() {
        return "技术支持联系方式：\n" +
               "电话：400-999-9999\n" +
               "微信：zanqi-support\n" +
               "邮箱：support@zanqi.com\n" +
               "工作时间：7×24小时";
    }

    @Tool(description = "构建硬件需求JSON")
    public String buildHardwareRequirements(Integer minCpuCores, String preferredCpu,
                                          Integer minMemory, Integer minGpuMemory, String preferredGpu,
                                          Integer minStorage, Integer minBandwidth) {
        return requirementsJsonUtil.buildHardwareRequirementsJson(
            minCpuCores, preferredCpu, minMemory, minGpuMemory, preferredGpu, minStorage, minBandwidth);
    }

    @Tool(description = "构建软件需求JSON")
    public String buildSoftwareRequirements(String preloadSoftware, String pluginRequirements, String customSoftware) {
        return requirementsJsonUtil.buildSoftwareRequirementsJson(
            List.of(preloadSoftware.split(",")), pluginRequirements, customSoftware);
    }

    @Tool(description = "构建服务需求JSON")
    public String buildServiceRequirements(String rentalType, Integer duration, 
                                        Boolean needTechSupport, Boolean needInstallation) {
        return requirementsJsonUtil.buildServiceRequirementsJson(
            rentalType, duration, needTechSupport, needInstallation);
    }
}