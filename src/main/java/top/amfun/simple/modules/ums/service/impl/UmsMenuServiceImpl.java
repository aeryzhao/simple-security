package top.amfun.simple.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import top.amfun.simple.modules.ums.dto.UmsMenuNote;
import top.amfun.simple.modules.ums.model.UmsMenu;
import top.amfun.simple.modules.ums.mapper.UmsMenuMapper;
import top.amfun.simple.modules.ums.service.UmsMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台菜单表 服务实现类
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
@Service
public class UmsMenuServiceImpl extends ServiceImpl<UmsMenuMapper, UmsMenu> implements UmsMenuService {

    @Override
    public boolean create(UmsMenu umsMenu) {
        umsMenu.setCreateTime(new Date());
        updateLevel(umsMenu);
        return save(umsMenu);
    }

    @Override
    public boolean updateMenu(Long menuId, UmsMenu umsMenu) {
        umsMenu.setId(menuId);
        updateLevel(umsMenu);
        return updateById(umsMenu);
    }

    @Override
    public Page<UmsMenu> list(Long parentId, Long pageNum, Long pageSize) {
        Page<UmsMenu> page = new Page<>(pageNum, pageSize);
        QueryWrapper<UmsMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsMenu::getParentId, parentId)
                .orderByAsc(UmsMenu::getSort);
        return page(page, wrapper);
    }

    @Override
    public boolean updateHidden(Long menuId, Integer hidden) {
        UmsMenu umsMenu = new UmsMenu();
        umsMenu.setId(menuId);
        umsMenu.setHidden(hidden);
        return updateById(umsMenu);
    }

    @Override
    public List<UmsMenuNote> treeList() {
        List<UmsMenu> menuList = list();
        List<UmsMenuNote> treeList = menuList.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> convertNote(menu, menuList)).collect(Collectors.toList());
        return treeList;
    }

    /**
     * 将UmsMenu转换为UmsMenuNote
     */
    private UmsMenuNote convertNote(UmsMenu menu, List<UmsMenu> menuList) {
        UmsMenuNote menuNote = new UmsMenuNote();
        BeanUtils.copyProperties(menu, menuNote);
        List<UmsMenuNote> childer = menuList.stream()
                .filter(submenu -> submenu.getParentId().equals(menu.getId()))
                .map(umsMenu -> convertNote(umsMenu, menuList)).collect(Collectors.toList());
        menuNote.setChilder(childer);
        return menuNote;
    }

    private void updateLevel(UmsMenu umsMenu) {
        if (umsMenu.getParentId() == 0) {
            umsMenu.setLevel(0);
        } else {
            UmsMenu parentMenu = getById(umsMenu.getParentId());
            if (parentMenu != null) {
                umsMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                umsMenu.setLevel(0);
            }
        }
    }
}
