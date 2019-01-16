package com.cxs.sys.sysuser.service.impl;

import com.cxs.core.baseframework.service.impl.BaseServiceImpl;
import com.cxs.sys.sysuser.dao.SysUserMapper;
import com.cxs.sys.sysuser.model.SysUserModel;
import com.cxs.sys.sysuser.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ChenXS
 * 系统级用户服务层实现类
 */
// Spring 注入
@Service("sysUserService")
// Dubbo 注入
//@com.alibaba.dubbo.config.annotation.Service(timeout = 1000)
public class SysUserServiceImpl extends BaseServiceImpl<SysUserModel> implements SysUserService {
    @Autowired
    private SysUserMapper userMapper;

    @Override
    public String getRole(String loginName) {
        return userMapper.getRole(loginName);
    }

    @Override
    public SysUserModel getUserModelWithBlurry(String loginName) {
        return userMapper.getUserModelWithBlurry(loginName);
    }
}
