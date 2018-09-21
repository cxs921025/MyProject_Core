package com.cxs.sys.sysrole.action;

import com.cxs.core.baseframework.action.BaseAction;
import com.cxs.sys.sysrole.model.SysRoleModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ChenXS
 * 系统级权限控制层
 */
@RestController
@RequestMapping("/sys/sysrole")
public class SysRoleAction extends BaseAction<SysRoleModel> {
}
