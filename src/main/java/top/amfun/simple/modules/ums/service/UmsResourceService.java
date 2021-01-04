package top.amfun.simple.modules.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.amfun.simple.modules.ums.model.UmsResource;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台资源表 服务类
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
public interface UmsResourceService extends IService<UmsResource> {

    boolean create(UmsResource umsResource);

    boolean update(Long resourceId, UmsResource umsResource);

    Page<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageNum, Integer pageSize);
}
