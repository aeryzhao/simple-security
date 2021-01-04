package top.amfun.simple.modules.ums.mapper;

import org.springframework.stereotype.Repository;
import top.amfun.simple.modules.ums.model.UmsAdminLoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 后台用户登录日志表 Mapper 接口
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
@Repository
public interface UmsAdminLoginLogMapper extends BaseMapper<UmsAdminLoginLog> {

}
