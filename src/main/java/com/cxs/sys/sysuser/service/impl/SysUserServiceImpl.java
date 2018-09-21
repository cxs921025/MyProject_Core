package com.cxs.sys.sysuser.service.impl;

import com.cxs.sys.sysuser.dao.SysUserMapper;
import com.cxs.sys.sysuser.model.SysUserModel;
import com.cxs.sys.sysuser.service.SysUserService;
import com.cxs.core.baseframework.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ChenXS
 * 系统级用户服务层实现类
 */
@Service("sysUserService")
public class SysUserServiceImpl extends BaseServiceImpl<SysUserModel> implements SysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public String getRole(String username) {
        return userMapper.getRole(username);
    }

    @Override
    public SysUserModel getUserModelWithBlurry(String username) {
        return userMapper.getUserModelWithBlurry(username);
    }
}
