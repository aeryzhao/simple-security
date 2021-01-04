package top.amfun.simple.modules.ums.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.amfun.simple.common.domain.CommonPage;
import top.amfun.simple.common.domain.CommonResult;
import top.amfun.simple.modules.ums.model.UmsMenu;
import top.amfun.simple.modules.ums.model.UmsResource;
import top.amfun.simple.modules.ums.model.UmsRole;
import top.amfun.simple.modules.ums.service.UmsRoleService;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 前端控制器
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
@Api(tags = "后台角色管理", description = "UmsRoleController")
@RestController
@RequestMapping("/ums/umsRole")
public class UmsRoleController {

    @Autowired
    private UmsRoleService umsRoleService;

    @ApiOperation(value = "添加角色")
    @PostMapping("/save")
    public CommonResult create(@RequestBody UmsRole umsRole) {
        boolean creaded = umsRoleService.create(umsRole);
        if (creaded) {
            return CommonResult.success("添加成功");
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "修改角色信息")
    @PutMapping("/{id}")
    public CommonResult update(@PathVariable("id") Long roleId,
                               @RequestBody UmsRole umsRole) {
        boolean updated = umsRoleService.update(roleId, umsRole);
        if (updated) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "批量删除角色")
    @DeleteMapping("/delete")
    public CommonResult batchDelete(@RequestParam("ids") List<Long> ids) {
        boolean removed = umsRoleService.removeByIds(ids);
        if (removed) {
            return CommonResult.success("删除成功");
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "获取全部角色")
    @GetMapping("/listAll")
    public CommonResult<List<UmsRole>> listAll () {
        List<UmsRole> roleList = umsRoleService.list();
        return CommonResult.success(roleList);
    }

    @ApiOperation(value = "分页角色名称模糊查询")
    @GetMapping("/list")
    public CommonResult<CommonPage<UmsRole>> list(@RequestParam(value = "name") String name,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        Page<UmsRole> page = umsRoleService.pageList(name, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @ApiOperation(value = "修改角色状态")
    @PutMapping("/status/{id}")
    public CommonResult updateStatus(@PathVariable("id") Long roleId,
                                     @RequestParam("status") Integer status) {
        boolean updated = umsRoleService.updateStatus(roleId, status);
        if (updated) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "获取角色菜单")
    @GetMapping("/listMenu/{id}")
    public CommonResult<List<UmsMenu>> listMenu(@PathVariable("id") Long roleId) {
        List<UmsMenu> menuList = umsRoleService.listMenu(roleId);
        return CommonResult.success(menuList);
    }

    @ApiOperation(value = "获取角色资源")
    @GetMapping("/listResource/{id}")
    public CommonResult<List<UmsResource>> listResource(@PathVariable("id") Long roleId) {
        List<UmsResource> resourceList = umsRoleService.listResource(roleId);
        return CommonResult.success(resourceList);
    }

    @ApiOperation(value = "分配角色菜单")
    @PostMapping("/allocMenu")
    public CommonResult allocMenu(@RequestParam("id") Long roleId,
                                  @RequestParam("menuIds") List<Long> menuIds) {
        boolean allocated = umsRoleService.allocMenu(roleId, menuIds);
        if (allocated) {
            return CommonResult.success("角色分配菜单成功");
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "分配角色资源")
    @PostMapping("allocResource")
    public CommonResult allocResource(@RequestParam("id") Long roleId,
                                      @RequestParam("resourceIds") List<Long> resourceIds) {
        boolean allocated = umsRoleService.allocResource(roleId, resourceIds);
        if (allocated) {
            return CommonResult.success("角色分配资源成功");
        }
        return CommonResult.failed();
    }
}

