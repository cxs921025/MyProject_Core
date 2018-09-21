package com.cxs.sys.sysuser.dao;

import com.cxs.core.baseframework.mapper.BaseMapper;
import com.cxs.sys.sysuser.model.SysUserModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

/**
 * @author ChenXS
 * 系统级用户数据访问层
 */
@Service
public interface SysUserMapper extends BaseMapper<SysUserModel> {

    public SysUserModel getUserModelWithBlurry(@Param("loginName") String loginName);

    public String getRole(@Param("loginName") String loginName);
}
