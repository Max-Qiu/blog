package com.maxqiu.blog.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.blog.entity.Label;
import com.maxqiu.blog.mapper.LabelMapper;

/**
 * 标签 服务类
 *
 * @author Max_Qiu
 */
@Service
public class LabelService extends ServiceImpl<LabelMapper, Label> {
    /**
     * 根据标签数量倒序返回列表
     */
    public List<Label> listByNum() {
        LambdaQueryWrapper<Label> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(Label::getCount);
        return list(wrapper);
    }

    /**
     * 管理端分页展示（按数量倒序）
     *
     * @param pageNumber
     *            页码
     * @param pageSize
     *            分页大小
     * @param name
     *            搜索名称
     */
    public Page<Label> pageQuery(Integer pageNumber, Integer pageSize, String name) {
        LambdaQueryWrapper<Label> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.hasText(name), Label::getName, name);
        wrapper.orderByDesc(Label::getCount);
        return page(new Page<>(pageNumber, pageSize), wrapper);
    }

    /**
     * 根据多标签ID查询标签名称
     *
     * 查询后，返回以 , 分隔的String字符串
     *
     * @param labelIds
     *            多标签ID（以 , 分割）
     */
    public String getLabelNamesByLabelIds(String labelIds) {
        return baseMapper.getLabelNamesByLabelIds(Arrays.asList(labelIds.split(",")));
    }

    /**
     * 刷新标签对应的文章数量
     */
    public void flushNum() {
        baseMapper.flushNum();
    }
}
