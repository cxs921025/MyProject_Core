package com.cxs.sys.sysuser.service;

import com.cxs.sys.sysuser.model.SysUserModel;
import com.cxs.core.baseframework.service.BaseService;

/**
 * @author ChenXS
 * 系统级用户服务层
 */
@SuppressWarnings("all")
public interface SysUserService extends BaseService<SysUserModel> {
    /**
     * 获取用户权限
     *
     * @param loginName 登录用户名
     * @return 用户对应权限
     */
    String getRole(String loginName);

    /**
     * 获取用户密码
     *
     * @param loginName 登录用户名
     * @return 用户实体
     */
    SysUserModel getUserModelWithBlurry(String loginName);

    /**
     * 测试事务
     */
    void saveUserAndRole();
}
