package com.maxqiu.blog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maxqiu.blog.entity.Label;

/**
 * 标签 Mapper 接口
 *
 * @author Max_Qiu
 */
public interface LabelMapper extends BaseMapper<Label> {
    /**
     * 根据多标签ID查询标签名称
     *
     * 查询后，返回以 , 分隔的String字符串
     *
     * @param labelIds
     *            标签ID集合
     */
    String getLabelNamesByLabelIds(@Param("labelIds") List<String> labelIds);

    /**
     * 刷新标签对应的文章数量
     */
    void flushNum();
}
