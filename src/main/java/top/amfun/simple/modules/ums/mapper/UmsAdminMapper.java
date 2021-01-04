package top.amfun.simple.modules.ums.mapper;

import org.springframework.stereotype.Repository;
import top.amfun.simple.modules.ums.model.UmsAdmin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.amfun.simple.modules.ums.model.UmsRole;

import java.util.List;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
@Repository
public interface UmsAdminMapper extends BaseMapper<UmsAdmin> {

    List<UmsRole> getRoleList(Long adminId);
}
