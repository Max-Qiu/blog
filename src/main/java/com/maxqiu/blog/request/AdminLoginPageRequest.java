package com.maxqiu.blog.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 管理员 - 登录
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
public class AdminLoginPageRequest {
    /**
     * 用户名
     */
    @NotBlank
    private String username;

    /**
     * 密码
     */
    @NotBlank
    private String password;
}
