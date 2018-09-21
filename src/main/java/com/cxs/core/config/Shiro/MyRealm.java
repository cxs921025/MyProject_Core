package com.cxs.core.config.Shiro;

import com.cxs.core.utils.LogUtil;
import com.cxs.sys.sysuser.model.SysUserModel;
import com.cxs.sys.sysuser.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ChenXS
 * Shiro 自定义身份认证
 */
public class MyRealm extends AuthorizingRealm {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取身份验证信息
     * Shiro中，最终是通过 Realm 来获取应用程序中的用户、角色及权限信息的。
     *
     * @param authenticationToken 用户身份信息 token
     * @return 返回封装了用户信息的 AuthenticationInfo 实例
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        LogUtil.info("[身份认证]: " + token.getUsername());
        // 从数据库获取对应用户名密码的用户
        SysUserModel user = sysUserService.getUserModelWithBlurry(token.getUsername());
        if (null == user.getPassword()) {
            LogUtil.error("[身份认证]: 用户名不正确");
            throw new AccountException("用户名或密码不正确！");
        } else if (!user.getPassword().equals(new String((char[]) token.getCredentials()))) {
            LogUtil.error("[身份认证]: 密码不正确");
            throw new AccountException("用户名或密码不正确!");
        }
        LogUtil.info("[身份认证]: 认证通过");
        return new SimpleAuthenticationInfo(token.getPrincipal(), user.getPassword(), getName());
    }

    /**
     * 获取授权信息
     *
     * @param  principalCollection principalCollection
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        LogUtil.info("[权限认证]: " + username);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获得该用户角色
        String role = sysUserService.getRole(username);
        Set<String> set = new HashSet<>();
        //需要将 role 封装到 Set 作为 info.setRoles() 的参数
        set.add(role);
        //设置该用户拥有的角色
        info.setRoles(set);
        LogUtil.info("[权限认证]: 认证通过; 权限: " + role);
        return info;
    }
}