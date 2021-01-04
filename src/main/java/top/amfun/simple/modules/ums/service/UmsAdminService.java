package top.amfun.simple.modules.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import top.amfun.simple.modules.ums.dto.UmsAdminParam;
import top.amfun.simple.modules.ums.dto.UpdateAdminPasswordParam;
import top.amfun.simple.modules.ums.model.UmsAdmin;
import com.baomidou.mybatisplus.extension.service.IService;
import top.amfun.simple.modules.ums.model.UmsResource;
import top.amfun.simple.modules.ums.model.UmsRole;

import java.util.List;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
public interface UmsAdminService extends IService<UmsAdmin> {

    String login(String username, String password);

    /**
     * 根据adminId查询资源
     * @param adminId
     * @return
     */
    List<UmsResource> getResourceList(Long adminId);
    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    UmsAdmin getAdminByUsername(String username);
    /**
     * 获取用户信息
     * @param username
     * @return
     */
    UserDetails loadUserByUsername(String username);

    UmsAdmin registry(UmsAdminParam umsAdminParam);

    List<UmsRole> getRoleList(Long adminId);

    Page<UmsAdmin> list(String keyword, int pageNum, int pageSize);

    boolean update(Long adminId, UmsAdmin umsAdmin);

    int updatePassword(UpdateAdminPasswordParam updateAdminPasswordParam);

    /**
     * 修改用户角色关系
     * @param adminId
     * @param roleIds
     */
    @Transactional
    boolean updateRoles(Long adminId, List<Long> roleIds);
}
