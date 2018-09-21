package com.cxs.core.utils;

import com.cxs.sys.sysuser.model.SysUserModel;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * @author ChenXS
 * Session工具类
 */
public class SessionUtil {

    /**
     * 不允许实例化
     */
    private SessionUtil() {
    }

    /**
     * 获取session
     *
     * @return Session
     */
    public static Session getSession() {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.getSession();
    }

    /**
     * 获取session中存储的用户信息
     *
     * @return SysUserModel
     */
    public static SysUserModel getSessionUserModel() {
        Session session = getSession();
        return (SysUserModel) session.getAttribute("SysUserModel");
    }
}
