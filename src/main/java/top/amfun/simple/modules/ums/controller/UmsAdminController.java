package top.amfun.simple.modules.ums.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import top.amfun.simple.common.domain.CommonResult;
import top.amfun.simple.modules.ums.dto.UmsAdminLoginParam;
import top.amfun.simple.modules.ums.dto.UmsAdminParam;
import top.amfun.simple.modules.ums.dto.UpdateAdminPasswordParam;
import top.amfun.simple.modules.ums.model.UmsAdmin;
import top.amfun.simple.modules.ums.model.UmsRole;
import top.amfun.simple.modules.ums.service.UmsAdminService;
import top.amfun.simple.modules.ums.service.UmsRoleService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台用户管理
 * @author will
 * @since 2020-10-26
 */
@Api(tags = "后台用户管理", description = "UmsAdminController")
@RestController
@RequestMapping("/admin")
public class UmsAdminController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private UmsAdminService umsAdminService;
    @Autowired
    private UmsRoleService umsRoleService;

    @ApiOperation(value = "用户注册")
    @ApiImplicitParam(name = "umsAdminParam", value = "注册信息", dataType = "UmsAdminParam", paramType = "body")
    @PostMapping(value = "/registry")
    public CommonResult<UmsAdmin> registry(@Validated @RequestBody UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = umsAdminService.registry(umsAdminParam);
        if (umsAdmin == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(umsAdmin);
    }

    @ApiOperation(value = "登录后返回token")
    @ApiImplicitParam(name = "umsAdminLoginParam", value = "用户名和密码", required = true, dataType = "UmsAdminLoginParam", paramType = "body")
    @PostMapping(value = "/login")
    public CommonResult login(@Validated @RequestBody UmsAdminLoginParam umsAdminLoginParam) {
        String token = umsAdminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        HashMap<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "登录后获取用户信息")
    @GetMapping(value = "/info")
    public CommonResult info(Principal principal) {
        if (principal == null) {
            return CommonResult.unauthorized(null);
        }
        String username = principal.getName();
        UmsAdmin umsAdmin = umsAdminService.getAdminByUsername(username);
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", umsAdmin.getUsername());
        map.put("menus", umsRoleService.getMenuList(umsAdmin.getId()));
        map.put("icon", umsAdmin.getIcon());
        List<UmsRole> roleList = umsAdminService.getRoleList(umsAdmin.getId());
        if (CollUtil.isNotEmpty(roleList)) {
            List<String> roles = roleList.stream().map(role -> role.getName()).collect(Collectors.toList());
            map.put("roles", roles);
        }
        return CommonResult.success(map);
    }

    @ApiOperation(value = "根据用户名或昵称获取用户列表")
    @GetMapping("/list")
    public CommonResult list(@RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        Page<UmsAdmin> adminList = umsAdminService.list(keyword, pageNum, pageSize);
        return CommonResult.success(adminList);
    }

    @ApiOperation(value = "获取指定id用户信息")
    @GetMapping("/{id}")
    public CommonResult getItem(@PathVariable("id") Long adminId) {
        UmsAdmin umsAdmin = umsAdminService.getById(adminId);
        return CommonResult.success(umsAdmin);
    }

    @ApiOperation(value = "修改个人信息")
    @PutMapping("/{id}")
    public CommonResult updateItem(@PathVariable("id") Long adminId, @RequestBody UmsAdmin umsAdmin) {
        boolean flag = umsAdminService.update(adminId, umsAdmin);
        if (flag) {
            return CommonResult.success("修改成功");
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "修改个人密码")
    @PutMapping("/password")
    public CommonResult updatePassword(@Validated @RequestBody UpdateAdminPasswordParam updateAdminPasswordParam) {
        int status = umsAdminService.updatePassword(updateAdminPasswordParam);
        if (status == 0) {
            return CommonResult.success(status);
        } else if (status == -1) {
            return CommonResult.failed("提交参数不合法");
        } else if (status == -2) {
            return CommonResult.failed("用户不存在");
        } else if (status == -3) {
            return CommonResult.failed("原密码错误");
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable("id") Long adminId) {
        boolean remove = umsAdminService.removeById(adminId);
        if (remove) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改用户账号状态")
    @PutMapping("/status/{id}")
    public CommonResult updateStatus(@PathVariable("id") Long adminId,
                                     @RequestParam(value = "status", required = true) Integer status) {
        UmsAdmin umsAdmin = new UmsAdmin();
        umsAdmin.setId(adminId);
        umsAdmin.setStatus(status);
        boolean update = umsAdminService.updateById(umsAdmin);
        if (update) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "分配用户角色")
    @PutMapping("/role")
    public CommonResult roleUpdate(@RequestParam("adminId") Long adminId,
                                   @RequestParam("roleIds") List<Long> roleIds) {
        boolean flag = umsAdminService.updateRoles(adminId, roleIds);
        if (flag) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "获取用户角色")
    @GetMapping("/role/{adminId}")
    public CommonResult getRoleList(@PathVariable("adminId") Long adminId) {
        List<UmsRole> roleList = umsAdminService.getRoleList(adminId);
        return CommonResult.success(roleList);
    }
}

