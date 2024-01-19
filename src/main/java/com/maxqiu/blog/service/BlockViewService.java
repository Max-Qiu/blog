package com.maxqiu.blog.service;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.blog.entity.BlockView;
import com.maxqiu.blog.mapper.BlockViewMapper;

/**
 * 屏蔽访问 服务类
 *
 * @author Max_Qiu
 */
@Service
@CacheConfig(cacheNames = "block-view")
public class BlockViewService extends ServiceImpl<BlockViewMapper, BlockView> {
    /**
     * 按优先级排序
     */
    @Cacheable(key = "'listOrderByCount'", cacheManager = "expire1day")
    public List<BlockView> listOrderByCount() {
        return baseMapper.listOrderByCount();
    }
}
