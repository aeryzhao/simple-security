package top.amfun.simple.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import top.amfun.simple.modules.ums.mapper.*;
import top.amfun.simple.modules.ums.model.*;
import top.amfun.simple.modules.ums.service.UmsRoleMenuRelationService;
import top.amfun.simple.modules.ums.service.UmsRoleResourceRelationService;
import top.amfun.simple.modules.ums.service.UmsRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 后台用户角色表 服务实现类
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
@Service
public class UmsRoleServiceImpl extends ServiceImpl<UmsRoleMapper, UmsRole> implements UmsRoleService {

    @Autowired
    private UmsMenuMapper umsMenuMapper;
    @Autowired
    private UmsResourceMapper umsResourceMapper;
    @Autowired
    private UmsRoleResourceRelationService umsRoleResourceRelationService;
    @Autowired
    private UmsRoleMenuRelationService umsRoleMenuRelationService;
    @Override
    public List<UmsMenu> getMenuList(Long id) {
        return umsMenuMapper.getMenuList(id);
    }

    @Override
    public boolean create(UmsRole umsRole) {
        umsRole.setCreateTime(new Date());
        return save(umsRole);
    }

    @Override
    public boolean update(Long roleId, UmsRole umsRole) {
        umsRole.setId(roleId);
        return updateById(umsRole);
    }

    @Override
    public Page<UmsRole> pageList(String name, Integer pageNum, Integer pageSize) {
        Page<UmsRole> page = new Page<>(pageNum, pageSize);
        QueryWrapper<UmsRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(UmsRole::getName, name);
        return page(page,wrapper);
    }

    @Override
    public boolean updateStatus(Long roleId, Integer status) {
        UmsRole umsRole = getById(roleId);
        umsRole.setStatus(status);
        return updateById(umsRole);
    }

    @Override
    public List<UmsMenu> listMenu(Long roleId) {
        List<UmsMenu> menuList = umsMenuMapper.getMenuListByRoleId(roleId);
        return menuList;
    }

    @Override
    public List<UmsResource> listResource(Long roleId) {
        List<UmsResource> resourceList = umsResourceMapper.getResourceListByRoleId(roleId);
        return resourceList;
    }

    @Override
    public boolean allocMenu(Long roleId, List<Long> menuIds) {
        // 删除原来关系
        QueryWrapper<UmsRoleMenuRelation> relationQueryWrapper = new QueryWrapper<>();
        relationQueryWrapper.lambda().eq(UmsRoleMenuRelation::getRoleId, roleId);
        umsRoleMenuRelationService.removeById(roleId);
        // 插入新关系
        ArrayList<UmsRoleMenuRelation> relations = new ArrayList<>();
        for (Long menuId : menuIds) {
            UmsRoleMenuRelation umsRoleMenuRelation = new UmsRoleMenuRelation();
            umsRoleMenuRelation.setMenuId(menuId);
            umsRoleMenuRelation.setRoleId(roleId);
            relations.add(umsRoleMenuRelation);
        }
        return umsRoleMenuRelationService.saveBatch(relations);
    }

    @Override
    public boolean allocResource(Long roleId, List<Long> resourceIds) {
        // 删除原关系
        QueryWrapper<UmsRoleResourceRelation> relationQueryWrapper = new QueryWrapper<>();
        relationQueryWrapper.lambda().eq(UmsRoleResourceRelation::getRoleId, roleId);
        umsRoleResourceRelationService.remove(relationQueryWrapper);
        // 建立新关系
        ArrayList<UmsRoleResourceRelation> relations = new ArrayList<>();
        for (Long resourceId : resourceIds) {
            UmsRoleResourceRelation relation = new UmsRoleResourceRelation();
            relation.setRoleId(roleId);
            relation.setResourceId(resourceId);
            relations.add(relation);
        }
        return umsRoleResourceRelationService.saveBatch(relations);
    }
}
