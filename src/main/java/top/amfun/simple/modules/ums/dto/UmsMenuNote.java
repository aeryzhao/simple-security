package top.amfun.simple.modules.ums.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import top.amfun.simple.modules.ums.model.UmsMenu;

import java.util.List;

/**
 * @date 2020/11/4 11:12
 * @description: 菜单节点封装
 */
@Getter
@Setter
public class UmsMenuNote extends UmsMenu {
    @ApiModelProperty(value = "子菜单")
    private List<UmsMenuNote> childer;
}
