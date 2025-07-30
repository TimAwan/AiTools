package com.ght666.aiTools.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ght666.aiTools.entity.po.Course;
import com.ght666.aiTools.mapper.CourseMapper;
import com.ght666.aiTools.service.ICourseService;
import org.springframework.stereotype.Service;

/**
 * 学科表 服务实现类
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

}