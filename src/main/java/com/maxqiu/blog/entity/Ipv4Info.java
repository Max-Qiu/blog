package com.maxqiu.blog.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
 * IP地址信息缓存
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("ipv4_info")
public class Ipv4Info extends Model<Ipv4Info> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（即IPV4转long）
     */
    @TableId("id")
    private Long id;

    /**
     * 国家
     */
    @TableField("country")
    private String country;

    /**
     * 省份/自治区/直辖市
     */
    @TableField("province")
    private String province;

    /**
     * 地级市
     */
    @TableField("city")
    private String city;

    /**
     * 区/县
     */
    @TableField("county")
    private String county;

    /**
     * 运营商
     */
    @TableField("operator")
    private String operator;

    /**
     * 邮政编码
     */
    @TableField("postal_code")
    private String postalCode;

    /**
     * 地区区号
     */
    @TableField("area_code")
    private String areaCode;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
