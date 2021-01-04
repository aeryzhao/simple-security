package top.amfun.simple.modules.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.amfun.simple.modules.ums.dto.UmsMenuNote;
import top.amfun.simple.modules.ums.model.UmsMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 后台菜单表 服务类
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
public interface UmsMenuService extends IService<UmsMenu> {

    boolean create(UmsMenu umsMenu);

    boolean updateMenu(Long menuId, UmsMenu umsMenu);

    Page<UmsMenu> list(Long parentId, Long pageNum, Long pageSize);

    boolean updateHidden(Long menuId, Integer hidden);

    List<UmsMenuNote> treeList();
}
