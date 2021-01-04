package top.amfun.simple.modules.ums.mapper;

import org.springframework.stereotype.Repository;
import top.amfun.simple.modules.ums.model.UmsMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 后台菜单表 Mapper 接口
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
@Repository
public interface UmsMenuMapper extends BaseMapper<UmsMenu> {

    List<UmsMenu> getMenuList(Long adminId);

    List<UmsMenu> getMenuListByRoleId(Long roleId);
}
