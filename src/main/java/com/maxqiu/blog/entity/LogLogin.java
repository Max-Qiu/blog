package com.maxqiu.blog.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 登录日志
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("log_login")
public class LogLogin extends Model<LogLogin> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 登录IP
     */
    @TableField("ip")
    private String ip;

    /**
     * 创建时间（登录时间）
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
