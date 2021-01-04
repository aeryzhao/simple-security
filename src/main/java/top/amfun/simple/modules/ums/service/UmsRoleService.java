package top.amfun.simple.modules.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.amfun.simple.modules.ums.model.UmsMenu;
import top.amfun.simple.modules.ums.model.UmsResource;
import top.amfun.simple.modules.ums.model.UmsRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 服务类
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
public interface UmsRoleService extends IService<UmsRole> {

    /**
     * @param id userId
     */
    List<UmsMenu> getMenuList(Long id);

    boolean create(UmsRole umsRole);

    boolean update(Long roleId, UmsRole umsRole);

    Page<UmsRole> pageList(String name, Integer pageNum, Integer pageSize);

    boolean updateStatus(Long roleId, Integer status);

    List<UmsMenu> listMenu(Long roleId);

    List<UmsResource> listResource(Long roleId);

    boolean allocMenu(Long roleId, List<Long> menuIds);

    boolean allocResource(Long roleId, List<Long> resourceIds);
}
