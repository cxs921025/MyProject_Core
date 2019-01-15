package com.cxs.sys.sysuser.service;

import com.cxs.sys.sysuser.model.SysUserModel;
import com.cxs.core.baseframework.service.BaseService;

/**
 * @author ChenXS
 * 系统级用户服务层
 */
public interface SysUserService extends BaseService<SysUserModel> {
    /**
     * 获取用户权限
     *
     * @param username 用户名
     * @return 用户对应权限
     */
    String getRole(String username);

    /**
     * 获取用户密码
     *
     * @param username 用户名
     * @return 用户实体
     */
    SysUserModel getUserModelWithBlurry(String username);

    /**
     * 测试事务
     */
    void saveUserAndRole();
}
