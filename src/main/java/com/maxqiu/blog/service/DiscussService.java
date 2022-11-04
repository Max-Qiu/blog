package com.maxqiu.blog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.blog.entity.Discuss;
import com.maxqiu.blog.mapper.DiscussMapper;
import com.maxqiu.blog.pojo.vo.DiscussVO;

/**
 * 评论 服务类
 *
 * @author Max_Qiu
 */
@Service
public class DiscussService extends ServiceImpl<DiscussMapper, Discuss> {
    /**
     * 根据文章ID获取评论（树）
     *
     * @param articleId
     *            文章ID
     * @param all
     *            是否包含全部
     */
    public List<DiscussVO> list(Integer articleId, boolean all) {
        LambdaQueryWrapper<Discuss> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Discuss::getArticleId, articleId);
        if (!all) {
            wrapper.eq(Discuss::getCheck, 1);
        }
        return listToTree(list(wrapper), 0);
    }

    /**
     * 树形结构转换
     *
     * @param fullList
     *            完整列表
     * @param revertId
     *            被回复的ID
     */
    private List<DiscussVO> listToTree(List<Discuss> fullList, Integer revertId) {
        return fullList.stream()
            // 过滤
            .filter(discuss -> discuss.getRevertId().equals(revertId))
            // 转换
            .map(discuss -> new DiscussVO(discuss, listToTree(fullList, discuss.getId())))
            // 收集
            .collect(Collectors.toList());
    }

    /**
     * 评论分页数据（管理端）
     *
     * @param pageNumber
     *            页码
     * @param pageSize
     *            页面大小
     * @param check
     *            是否审核（null为全部）
     */
    public Page<Discuss> managerPage(Integer pageNumber, Integer pageSize, Boolean check) {
        LambdaQueryWrapper<Discuss> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(check != null, Discuss::getCheck, check);
        wrapper.orderByDesc(Discuss::getId);
        return page(new Page<>(pageNumber, pageSize), wrapper);
    }

    /**
     * 新增评论
     *
     * @param articleId
     *            文章ID
     * @param nickname
     *            用户昵称
     * @param content
     *            评论内容
     * @param revertId
     *            回复ID
     */
    public boolean form(Integer articleId, String nickname, String content, Integer revertId) {
        Discuss discuss = new Discuss();
        discuss.setArticleId(articleId);
        discuss.setNickname(nickname);
        discuss.setContent(content);
        discuss.setRevertId(revertId);
        if (discuss.getRevertId() == 0) {
            discuss.setTier(0);
        } else {
            Discuss parentDiscuss = getById(discuss.getRevertId());
            discuss.setTier(parentDiscuss.getTier() + 1);
        }
        discuss.setCheck(false);
        return discuss.insert();
    }

    /**
     * 根据多个ID删除自己并删除自己的子评论
     *
     * @param discussIdList
     *            ID字符串
     */
    public void deleteByIds(List<Integer> discussIdList) {
        deleteThisAndChildByIds(discussIdList);
    }

    /**
     * 根据多个ID删除自己并删除自己的子评论
     *
     * @param discussIdList
     *            多个ID集合
     */
    private void deleteThisAndChildByIds(List<Integer> discussIdList) {
        // 删自己
        removeByIds(discussIdList);
        // 查找自己儿子
        List<Discuss> childList = baseMapper.findByIds(discussIdList);
        if (childList.size() != 0) {
            List<Integer> childIds = new ArrayList<>();
            for (Discuss discuss : childList) {
                childIds.add(discuss.getId());
            }
            deleteThisAndChildByIds(childIds);
        }
    }

    /**
     * 变更评论状态
     *
     * @param discussId
     *            评论ID
     * @param check
     *            审核状态
     */
    public void changeCheck(Integer discussId, Boolean check) {
        Discuss discuss = getById(discussId);
        if (check) {
            check(discuss);
        } else {
            unCheck(discuss);
        }
    }

    /**
     * 屏蔽评论
     *
     * @param discuss
     *            评论
     */
    private void unCheck(Discuss discuss) {
        discuss.setCheck(false);
        discuss.updateById();
        for (Discuss childDiscuss : getChildList(discuss.getId())) {
            unCheck(childDiscuss);
        }
    }

    /**
     * 根据父评论ID获取子评论ID
     *
     * @param parentId
     *            父评论ID
     */
    private List<Discuss> getChildList(Integer parentId) {
        LambdaQueryWrapper<Discuss> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Discuss::getRevertId, parentId);
        return list(wrapper);
    }

    /**
     * 通过评论
     *
     * @param discuss
     *            评论
     */
    private void check(Discuss discuss) {
        discuss.setCheck(true);
        discuss.updateById();
        Discuss parentDiscuss = getById(discuss.getRevertId());
        if (parentDiscuss != null) {
            check(parentDiscuss);
        }
    }
}
