package com.cxs.sys.sysuser.service.impl;

import com.cxs.core.baseframework.service.impl.BaseServiceImpl;
import com.cxs.core.utils.BaseUtil;
import com.cxs.sys.sysrole.model.SysRoleModel;
import com.cxs.sys.sysrole.service.SysRoleService;
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

    private final SysUserMapper userMapper;
    private final SysRoleService roleService;

    @Autowired
    public SysUserServiceImpl(SysUserMapper userMapper, SysRoleService roleService) {
        this.userMapper = userMapper;
        this.roleService = roleService;
    }

    @Override
    public String getRole(String loginName) {
        return userMapper.getRole(loginName);
    }

    @Override
    public SysUserModel getUserModelWithBlurry(String loginName) {
        return userMapper.getUserModelWithBlurry(loginName);
    }

    @Override
    public void saveUserAndRole() {
        SysUserModel userModel = SysUserModel.builder().userName("测试事务").loginName("tx1").email("tx@123.com").password(BaseUtil.encryptionWithMd5("123456")).build();
        this.save(userModel);
        SysRoleModel roleModel = SysRoleModel.builder().code("tx1").name("测试事务").build();
        roleService.save(roleModel);
    }
}
