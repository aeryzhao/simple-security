package top.amfun.simple.modules.ums.mapper;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.amfun.simple.modules.ums.model.UmsResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 后台资源表 Mapper 接口
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
@Repository
public interface UmsResourceMapper extends BaseMapper<UmsResource> {

    List<UmsResource> getResourceList(@Param("adminId") Long adminId);

    List<UmsResource> getResourceListByRoleId(Long roleId);
}
