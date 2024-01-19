package com.maxqiu.blog.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maxqiu.blog.entity.BlockView;

/**
 * 屏蔽访问 Mapper 接口
 *
 * @author Max_Qiu
 */
public interface BlockViewMapper extends BaseMapper<BlockView> {
    List<BlockView> listOrderByCount();
}
