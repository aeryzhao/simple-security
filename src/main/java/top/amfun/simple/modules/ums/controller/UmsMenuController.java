package top.amfun.simple.modules.ums.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.amfun.simple.common.domain.CommonPage;
import top.amfun.simple.common.domain.CommonResult;
import top.amfun.simple.modules.ums.dto.UmsMenuNote;
import top.amfun.simple.modules.ums.model.UmsMenu;
import top.amfun.simple.modules.ums.service.UmsMenuService;

import java.util.List;

/**
 * <p>
 * 后台菜单表 前端控制器
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
@Api(tags = "后台菜单管理")
@RestController
@RequestMapping("/ums/umsMenu")
public class UmsMenuController {

    @Autowired
    private UmsMenuService umsMenuService;

    @ApiOperation(value = "创建菜单")
    @PostMapping("/save")
    public CommonResult create(@RequestBody UmsMenu umsMenu) {
        boolean creaded = umsMenuService.create(umsMenu);
        if (creaded) {
            return CommonResult.success("创建成功");
        }
        return CommonResult.failed("创建失败");
    }

    @ApiOperation(value = "根据ID获取后台菜单信息")
    @GetMapping("/{id}")
    public CommonResult getItem(@PathVariable("id") Long menuId) {
        UmsMenu umsMenu = umsMenuService.getById(menuId);
        return CommonResult.success(umsMenu);
    }

    @ApiOperation(value = "根据ID修改后台菜单信息")
    @PutMapping("/{id}")
    public CommonResult updateMenu(@PathVariable("id") Long menuId, @RequestBody UmsMenu umsMenu){
        boolean updated = umsMenuService.updateMenu(menuId, umsMenu);
        if (updated) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "根据ID删除后台菜单")
    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable("id") Long menuId) {
        boolean removed = umsMenuService.removeById(menuId);
        if (removed) {
            return CommonResult.success("删除成功");
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "分页查询后台菜单")
    @GetMapping("/list/{parentId}")
    public CommonResult<CommonPage<UmsMenu>> list(@PathVariable("parentId") Long parentId,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Long pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Long pageSize) {
        Page<UmsMenu> pageList = umsMenuService.list(parentId, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(pageList));
    }

    @ApiOperation(value = "树形结构返回全部菜单")
    @GetMapping("/treeList")
    public CommonResult<List<UmsMenuNote>> treeList() {
        List<UmsMenuNote> treeList = umsMenuService.treeList();
        return CommonResult.success(treeList);
    }

    @ApiOperation(value = "修改菜单显示状态,1是隐藏，0是显示")
    @PutMapping("/hidden/{id}")
    public CommonResult updateHidden(@PathVariable("id") Long menuId,
                                     @RequestParam("hidden") Integer hidden) {
        boolean updated = umsMenuService.updateHidden(menuId, hidden);
        if (updated) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

}

