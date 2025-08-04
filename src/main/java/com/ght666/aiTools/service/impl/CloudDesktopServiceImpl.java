package com.ght666.aiTools.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ght666.aiTools.entity.po.CloudDesktop;
import com.ght666.aiTools.mapper.CloudDesktopMapper;
import com.ght666.aiTools.service.ICloudDesktopService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 云桌面配置服务实现类
 */
@Service
public class CloudDesktopServiceImpl extends ServiceImpl<CloudDesktopMapper, CloudDesktop> implements ICloudDesktopService {
    
    @Override
    public List<CloudDesktop> findByType(String type) {
        return this.query()
                .eq("type", type)
                .eq("status", 1)
                .list();
    }
    
    @Override
    public List<CloudDesktop> findByStatus(Integer status) {
        return this.query()
                .eq("status", status)
                .list();
    }
    
    @Override
    public List<CloudDesktop> findAvailableConfigs() {
        return this.query()
                .eq("status", 1)
                .list();
    }
}