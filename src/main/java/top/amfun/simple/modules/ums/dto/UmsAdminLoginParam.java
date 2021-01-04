package top.amfun.simple.modules.ums.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * @date 2020/10/27 18:04
 * @description: 用户登录参数
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UmsAdminLoginParam {
    @NotEmpty
    @ApiModelProperty(value = "用户名", required = false)
    private String username;
    @NotEmpty
    @ApiModelProperty(value = "密码", required = false)
    private String password;
}
