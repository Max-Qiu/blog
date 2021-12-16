package com.maxqiu.blog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maxqiu.blog.entity.Discuss;

/**
 * 评论 Mapper 接口
 *
 * @author Max_Qiu
 */
public interface DiscussMapper extends BaseMapper<Discuss> {
    /**
     * 根据列表查找评论列表
     *
     * @param discussIdList
     *            ID集合
     */
    List<Discuss> findByIds(@Param("discussIdList") List<Integer> discussIdList);
}
