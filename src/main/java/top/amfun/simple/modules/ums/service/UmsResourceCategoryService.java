package top.amfun.simple.modules.ums.service;

import top.amfun.simple.modules.ums.model.UmsResourceCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 资源分类表 服务类
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
public interface UmsResourceCategoryService extends IService<UmsResourceCategory> {

    List<UmsResourceCategory> listAll();

    boolean create(UmsResourceCategory category);
}
