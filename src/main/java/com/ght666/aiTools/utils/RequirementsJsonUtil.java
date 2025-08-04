package com.ght666.aiTools.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RequirementsJsonUtil {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 构建基础信息JSON
     */
    public String buildBasicInfoJson(String customerName, String contactInfo, String useCase, String budget) {
        try {
            Map<String, Object> basicInfo = new HashMap<>();
            basicInfo.put("customerName", customerName);
            basicInfo.put("contactInfo", contactInfo);
            basicInfo.put("useCase", useCase);
            basicInfo.put("budget", budget);
            
            return objectMapper.writeValueAsString(basicInfo);
        } catch (Exception e) {
            log.error("构建基础信息JSON失败", e);
            return "{}";
        }
    }

    /**
     * 构建硬件需求JSON
     */
    public String buildHardwareRequirementsJson(Integer minCpuCores, String preferredCpu,
                                              Integer minMemory, Integer minGpuMemory, String preferredGpu,
                                              Integer minStorage, Integer minBandwidth) {
        try {
            Map<String, Object> requirements = new HashMap<>();
            
            // CPU需求
            Map<String, Object> cpu = new HashMap<>();
            cpu.put("minCores", minCpuCores);
            cpu.put("preferred", preferredCpu);
            requirements.put("cpu", cpu);
            
            // 内存需求
            Map<String, Object> memory = new HashMap<>();
            memory.put("minSize", minMemory);
            memory.put("unit", "GB");
            requirements.put("memory", memory);
            
            // GPU需求
            Map<String, Object> gpu = new HashMap<>();
            gpu.put("minMemory", minGpuMemory);
            gpu.put("preferred", preferredGpu);
            requirements.put("gpu", gpu);
            
            // 存储需求
            Map<String, Object> storage = new HashMap<>();
            storage.put("minSize", minStorage);
            storage.put("unit", "GB");
            requirements.put("storage", storage);
            
            // 网络需求
            Map<String, Object> network = new HashMap<>();
            network.put("minBandwidth", minBandwidth);
            network.put("unit", "Mbps");
            requirements.put("network", network);
            
            return objectMapper.writeValueAsString(requirements);
        } catch (Exception e) {
            log.error("构建硬件需求JSON失败", e);
            return "{}";
        }
    }

    /**
     * 构建软件需求JSON
     */
    public String buildSoftwareRequirementsJson(List<String> preloadSoftware, String pluginRequirements, String customSoftware) {
        try {
            Map<String, Object> software = new HashMap<>();
            software.put("preloadSoftware", preloadSoftware);
            software.put("pluginRequirements", pluginRequirements);
            software.put("customSoftware", customSoftware);
            
            return objectMapper.writeValueAsString(software);
        } catch (Exception e) {
            log.error("构建软件需求JSON失败", e);
            return "{}";
        }
    }

    /**
     * 构建服务需求JSON
     */
    public String buildServiceRequirementsJson(String rentalType, Integer duration, 
                                            Boolean needTechSupport, Boolean needInstallation) {
        try {
            Map<String, Object> service = new HashMap<>();
            service.put("rentalType", rentalType);
            service.put("duration", duration);
            service.put("needTechSupport", needTechSupport);
            service.put("needInstallation", needInstallation);
            
            return objectMapper.writeValueAsString(service);
        } catch (Exception e) {
            log.error("构建服务需求JSON失败", e);
            return "{}";
        }
    }

    /**
     * 解析需求JSON
     */
    public Map<String, Object> parseRequirementsJson(String requirementsJson) {
        try {
            return objectMapper.readValue(requirementsJson, Map.class);
        } catch (Exception e) {
            log.error("解析需求JSON失败", e);
            return new HashMap<>();
        }
    }
}