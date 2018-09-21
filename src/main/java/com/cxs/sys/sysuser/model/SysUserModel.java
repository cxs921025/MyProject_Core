package com.cxs.sys.sysuser.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cxs.core.baseframework.model.BaseModel;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ChenXS
 * 系统级用户实体
 * 对应数据库中SYS_USER表
 */
@Table(name = "SYS_USER")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class SysUserModel extends BaseModel {

    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;
    /**
     * 用户名
     */
    @Column(name = "USER_NAME")
    private String userName;
    /**
     * 登录名
     */
    @Column(name = "LOGIN_NAME")
    private String loginName;
    /**
     * 密码
     */
    @Column(name = "PASSWORD")
    private String password;
    /**
     * 手机号
     */
    @Column(name = "PHONE")
    private String phone;
    /**
     * 电子邮箱
     */
    @Column(name = "EMAIL")
    private String Email;
    /**
     * 权限表主键
     */
    @Column(name = "ROLE_ID")
    private String roleId;
    /**
     * 状态
     */
    @Column(name = "STATUS")
    private String status;
    /**
     * 是否更新密码
     */
    @Column(name = "IS_UPDATE_PWD")
    private String isUpdatePwd;
    /**
     * 备注
     */
    @Column(name = "REMARK")
    private String remark;
}
