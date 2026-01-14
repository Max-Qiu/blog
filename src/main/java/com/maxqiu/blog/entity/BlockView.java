package com.maxqiu.blog.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.maxqiu.blog.enums.BlockViewConditionEnum;
import com.maxqiu.blog.enums.BlockViewTypeEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 屏蔽访问
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("block_view")
public class BlockView extends Model<BlockView> {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 类型 1useragent 2ip
     */
    @TableField("`type`")
    private BlockViewTypeEnum type;

    /**
     * 条件 1= 2like
     */
    @TableField("`condition`")
    private BlockViewConditionEnum condition;

    /**
     * 值
     */
    @TableField("`value`")
    private String value;

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
