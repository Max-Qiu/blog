package com.maxqiu.blog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 标签管理 - 表单提交
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
public class AdminLabelFromRequest {
    /**
     * 标签主键
     */
    private Integer id;

    /**
     * 标签名称
     */
    @NotBlank
    private String name;
}
