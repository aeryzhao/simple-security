package top.amfun.simple.modules.ums.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.amfun.simple.common.domain.CommonPage;
import top.amfun.simple.common.domain.CommonResult;
import top.amfun.simple.modules.ums.model.UmsResource;
import top.amfun.simple.modules.ums.service.UmsResourceService;

import java.util.List;

/**
 * <p>
 * 后台资源表 前端控制器
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
@Api(tags = "后台资源管理")
@RestController
@RequestMapping("/ums/umsResource")
public class UmsResourceController {

    @Autowired
    private UmsResourceService umsResourceService;

    @ApiOperation(value = "添加后台资源")
    @PostMapping("/save")
    public CommonResult create(@RequestBody UmsResource umsResource) {
        boolean created = umsResourceService.create(umsResource);
        if (created) {
            return CommonResult.success("添加成功");
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "修改后台资源")
    @PutMapping("/{id}")
    public CommonResult update(@PathVariable("id") Long resourceId,
                               @RequestBody UmsResource umsResource) {
        boolean updated = umsResourceService.update(resourceId, umsResource);
        if (updated) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "根据ID获取资源信息")
    @GetMapping("/{id}")
    public CommonResult getItem(@PathVariable("id") Long resourceId) {
        UmsResource umsResource = umsResourceService.getById(resourceId);
        return CommonResult.success(umsResource);
    }

    @ApiOperation(value = "删除资源")
    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable("id") Long resourceId) {
        boolean removed = umsResourceService.removeById(resourceId);
        if (removed) {
            return CommonResult.success("删除成功");
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "分页模糊查询")
    @GetMapping("/list")
    public CommonResult<CommonPage<UmsResource>> list(@RequestParam(value = "categoryId", required = false) Long categoryId,
                                                      @RequestParam(value = "nameKeyword", required = false) String nameKeyword,
                                                      @RequestParam(value = "urlKeyword", required = false) String urlKeyword,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        Page<UmsResource> page = umsResourceService.list(categoryId, nameKeyword, urlKeyword, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @ApiOperation(value = "查询全部列表")
    @GetMapping("/listAll")
    public CommonResult<List<UmsResource>> listAll() {
        List<UmsResource> list = umsResourceService.list();
        return CommonResult.success(list);
    }
}

