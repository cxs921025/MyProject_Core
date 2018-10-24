package com.cxs.sys.sysuser.action;

import com.cxs.core.baseframework.action.BaseAction;
import com.cxs.sys.sysuser.rabbitmq.SysUserSender;
import com.cxs.core.exception.ServiceException;
import com.cxs.core.utils.SessionUtil;
import com.cxs.core.vo.ReturnVo;
import com.cxs.sys.sysuser.model.SysUserModel;
import com.cxs.sys.sysuser.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ChenXS
 * 系统级用户控制层
 */
@RequestMapping("/sys/sysuser")
@RestController
public class SysUserAction extends BaseAction<SysUserModel> {

    private final SysUserService sysUserService;
    private final SysUserSender sysUserSender;

    @Autowired
    public SysUserAction(SysUserService sysUserService, SysUserSender sysUserSender) {
        this.sysUserService = sysUserService;
        this.sysUserSender = sysUserSender;
    }

    @RequestMapping("getUser")
    public ReturnVo getUser() {
        ReturnVo aj = new ReturnVo();
        try {
            sysUserSender.getUserSender("admin");
            //SysUserModel build = SysUserModel.builder().loginName("admin").password(BaseUtil.encryptionWithMd5("12345")).userName("管理员").build();
            //List<SysUserModel> userModels = sysUserService.getAll();
            //aj.setObj(userModels);
            //SysUserModel userModel = SysUserModel.builder().id("07c11b5adfb242e48922774e6aae3691").loginName("ddd").userName("测试").password("123456").build();
            //SysUserModel userModel = SysUserModel.builder().loginName("aaa").userName("测试游客").build();
            //userModel = sysUserService.get(userModel);
            //userModel.setLoginName("123");
            //sysUserService.update(userModel);
            aj.setObj(SessionUtil.getSessionUserModel());
            aj.setMsg("getUser 成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            aj.setMsg(e.getMessage());
            aj.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aj;
    }
}
