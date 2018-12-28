package com.cxs.sys.sysuser.action;

import com.cxs.core.baseframework.action.BaseAction;
import com.cxs.core.exception.ServiceException;
import com.cxs.core.utils.LogUtil;
import com.cxs.core.utils.SessionUtil;
import com.cxs.core.vo.ReturnVo;
import com.cxs.sys.sysuser.model.SysUserModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ChenXS
 * 系统级用户控制层
 */
@RequestMapping("/sys/sysuser")
@RestController
public class SysUserAction extends BaseAction<SysUserModel> {

    @RequestMapping("getUser")
    public ReturnVo getUser() {
        ReturnVo aj = new ReturnVo();
        try {
            aj.setObj(SessionUtil.getSessionUserModel());
            aj.setMsg("getUser 成功");
        } catch (ServiceException e) {
            LogUtil.error(e);
            aj.setMsg(e.getMessage());
            aj.setSuccess(false);
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return aj;
    }
}
