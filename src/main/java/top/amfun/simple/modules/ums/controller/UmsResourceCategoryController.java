package top.amfun.simple.modules.ums.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.amfun.simple.common.domain.CommonResult;
import top.amfun.simple.modules.ums.model.UmsResourceCategory;
import top.amfun.simple.modules.ums.service.UmsResourceCategoryService;

import java.util.List;

/**
 * <p>
 * 资源分类表 前端控制器
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
@Api(tags = "资源分类管理", description = "UmsResourceCategoryController")
@RestController
@RequestMapping("/ums/umsResourceCategory")
public class UmsResourceCategoryController {

    @Autowired
    UmsResourceCategoryService categoryService;

    @ApiOperation(value = "查询资源全部分类")
    @GetMapping("/list")
    public CommonResult listAll() {
        List<UmsResourceCategory> categories = categoryService.listAll();
        return CommonResult.success(categories);
    }

    @ApiOperation(value = "创建资源分类")
    @PostMapping("/save")
    public CommonResult create(@RequestBody UmsResourceCategory category) {
        boolean creaded = categoryService.create(category);
        if (creaded) {
            return CommonResult.success("创建成功");
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "修改资源分类")
    @PutMapping("/{id}")
    public CommonResult update(@PathVariable("id") Long categoryId,
                               @RequestBody UmsResourceCategory category) {
        category.setId(categoryId);
        boolean updated = categoryService.updateById(category);
        if (updated) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "删除资源分类")
    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable("id") Long categoryId) {
        boolean removed = categoryService.removeById(categoryId);
        if (removed) {
            return CommonResult.success("删除成功");
        }
        return CommonResult.failed();
    }
}

