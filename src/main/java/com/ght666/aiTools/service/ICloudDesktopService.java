package com.ght666.aiTools.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ght666.aiTools.entity.po.CloudDesktop;

import java.util.List;

/**
 * 云桌面配置服务接口
 */
public interface ICloudDesktopService extends IService<CloudDesktop> {
    
    /**
     * 根据类型查询配置
     */
    List<CloudDesktop> findByType(String type);
    
    /**
     * 根据状态查询配置
     */
    List<CloudDesktop> findByStatus(Integer status);
    
    /**
     * 查询所有可用配置
     */
    List<CloudDesktop> findAvailableConfigs();
}