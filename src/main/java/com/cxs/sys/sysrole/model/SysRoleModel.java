package com.cxs.sys.sysrole.model;

import com.cxs.core.baseframework.model.BaseModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ChenXS
 * 系统级权限实体
 * 对应数据库中SYS_ROLE表
 */
@Table(name = "SYS_ROLE")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class SysRoleModel extends BaseModel {

    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;
    /**
     * 编码
     */
    @Column(name = "CODE")
    private String code;
    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 备注
     */
    @Column(name = "REMARK")
    private String remark;
}
