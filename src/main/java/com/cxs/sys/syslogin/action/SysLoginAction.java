package com.cxs.sys.syslogin.action;

import com.cxs.core.utils.BaseUtil;
import com.cxs.core.utils.LogUtil;
import com.cxs.core.vo.ReturnVo;
import com.cxs.sys.sysuser.model.SysUserModel;
import com.cxs.sys.sysuser.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChenXS
 * 系统级登录控制层
 */
@RestController
public class SysLoginAction {
    private final SysUserService sysUserService;

    @Autowired
    public SysLoginAction(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public ReturnVo login(SysUserModel userModel) {
        ReturnVo returnVo = new ReturnVo();
        LogUtil.info("[登录]: 开始");
        if (StringUtils.isAnyBlank(userModel.getLoginName(), userModel.getPassword())) {
            LogUtil.error("[登录]: 用户名或密码为空");
            returnVo.setMsg("用户名密码不能为空!");
            returnVo.setSuccess(false);
            LogUtil.error("[登录]: 结束");
            return returnVo;
        }
        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        // 在认证提交前准备 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(userModel.getLoginName(), BaseUtil.encryptionWithMd5(userModel.getPassword()));
        try {
            SysUserModel user = sysUserService.getUserModelWithBlurry(userModel.getLoginName());
            // 执行认证登陆
            subject.login(token);
            // 在session中存放用户信息
            LogUtil.info("[登录]: 将用户信息存放到session中");
            Session session = subject.getSession();
            session.setAttribute("SysUserModel", user);
            Map<String, Object> attributes = new HashMap<>();
            // 返回sessionId 作为token
            attributes.put("token", subject.getSession().getId());
            returnVo.setAttributes(attributes);
        } catch (AccountException e) {
            returnVo.setMsg(e.getMessage());
        }
        //根据权限，指定返回数据
        LogUtil.info("[登录]: 成功");
        return returnVo;
    }

    /**
     * 未登录
     */
    @RequestMapping("/403")
    public ReturnVo unauthorizedUrl() {
        ReturnVo returnVo = new ReturnVo();
        returnVo.setMsg("没有权限");
        returnVo.setSuccess(false);
        return returnVo;
    }

    /**
     * 出错处理
     */
    @RequestMapping("/noMapping")
    public ReturnVo noMapping() {
        ReturnVo returnVo = new ReturnVo();
        returnVo.setMsg("您访问的页面找不到了。。。。");
        returnVo.setSuccess(false);
        return returnVo;
    }

}
