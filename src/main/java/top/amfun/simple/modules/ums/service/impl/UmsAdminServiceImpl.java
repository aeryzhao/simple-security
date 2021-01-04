package top.amfun.simple.modules.ums.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.amfun.simple.common.exception.Asserts;
import top.amfun.simple.modules.ums.dto.UmsAdminParam;
import top.amfun.simple.modules.ums.dto.UpdateAdminPasswordParam;
import top.amfun.simple.modules.ums.mapper.UmsAdminLoginLogMapper;
import top.amfun.simple.modules.ums.mapper.UmsResourceMapper;
import top.amfun.simple.modules.ums.model.*;
import top.amfun.simple.modules.ums.mapper.UmsAdminMapper;
import top.amfun.simple.modules.ums.service.UmsAdminRoleRelationService;
import top.amfun.simple.modules.ums.service.UmsAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.amfun.simple.security.domain.AdminUserDetails;
import top.amfun.simple.security.util.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author will
 * @since 2020-10-26
 */
@Service
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminMapper, UmsAdmin> implements UmsAdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UmsResourceMapper umsResourceMapper;
    @Autowired
    private UmsAdminLoginLogMapper umsAdminLoginLogMapper;
    @Autowired
    private UmsAdminRoleRelationService umsAdminRoleRelationService;

    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                Asserts.fail("密码错误！");
            }
            if (!userDetails.isEnabled()) {
                Asserts.fail("账号被封禁");
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            token = jwtTokenUtil.generateToken(userDetails);
            insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常：{}", e.getMessage());
        }
        return token;
    }

    /**
     * 登录日志记录
     * @param username
     */
    public void insertLoginLog(String username) {
        UmsAdmin umsAdmin = getAdminByUsername(username);
        if (umsAdmin == null){
            return;
        }
        UmsAdminLoginLog umsAdminLoginLog = new UmsAdminLoginLog();
        umsAdminLoginLog.setAdminId(umsAdmin.getId());
        umsAdminLoginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        umsAdminLoginLog.setIp(request.getRemoteAddr());
        umsAdminLoginLogMapper.insert(umsAdminLoginLog);
    }

    @Override
    public List<UmsResource> getResourceList(Long adminId) {
        List<UmsResource> resourceList = umsResourceMapper.getResourceList(adminId);
        return resourceList;
    }

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername, username);
        List<UmsAdmin> umsAdminList = list(wrapper);
        if (umsAdminList != null && umsAdminList.size() > 0){
            UmsAdmin umsAdmin = umsAdminList.get(0);
            return umsAdmin;
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 查询UmsAdmin的完整信息
        UmsAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            List<UmsResource> resourceList = getResourceList(admin.getId());
            return new AdminUserDetails(admin, resourceList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public UmsAdmin registry(UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        // 查询用户是否存在
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername, umsAdmin.getUsername());
        List<UmsAdmin> umsAdminList = list(wrapper);
        if (umsAdminList.size() >0) {
            return null;
        }
        // 密码加密处理
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        baseMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return baseMapper.getRoleList(adminId);
    }

    @Override
    public Page<UmsAdmin> list(String keyword, int pageNum, int pageSize) {
        Page<UmsAdmin> page = new Page<>(pageNum, pageSize);
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(keyword)) {
            wrapper.lambda().like(UmsAdmin::getUsername, keyword)
                    .or().like(UmsAdmin::getNickName, keyword);
        }
        return page(page, wrapper);
    }

    @Override
    public boolean update(Long adminId, UmsAdmin umsAdmin) {
        umsAdmin.setId(adminId);
        UmsAdmin rawAdmin = baseMapper.selectById(adminId);
        if (!rawAdmin.getPassword().equals(umsAdmin.getPassword())) {
            umsAdmin.setPassword(passwordEncoder.encode(umsAdmin.getPassword()));
        }
        boolean success = updateById(umsAdmin);
        return success;
    }

    @Override
    public int updatePassword(UpdateAdminPasswordParam param) {
        if (StrUtil.isEmpty(param.getUsername())
        || StrUtil.isEmpty(param.getPassword())
        || StrUtil.isEmpty(param.getNewPassword())) {
            return -1;
        }
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername, param.getUsername());
        List<UmsAdmin> list = list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return -2;
        }
        UmsAdmin umsAdmin = list.get(0);
        if (!passwordEncoder.matches(param.getPassword(), umsAdmin.getPassword())) {
            return -3;
        }
        umsAdmin.setPassword(passwordEncoder.encode(param.getNewPassword()));
        updateById(umsAdmin);
        return 0;
    }

    @Override
    public boolean updateRoles(Long adminId, List<Long> roleIds) {
        int count = roleIds == null? 0 : roleIds.size();
        // 删除原先关联关系
        QueryWrapper<UmsAdminRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdminRoleRelation::getAdminId, adminId);
        umsAdminRoleRelationService.remove(wrapper);
        // 建立新关联
        if (!CollUtil.isEmpty(roleIds)) {
            ArrayList<UmsAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId:roleIds) {
                UmsAdminRoleRelation relation = new UmsAdminRoleRelation();
                relation.setAdminId(adminId);
                relation.setRoleId(roleId);
                list.add(relation);
            }
            umsAdminRoleRelationService.saveBatch(list);
        }
        return count > 0;
    }

}
