package com.cxs.sys.sysuser.task;

import com.cxs.core.utils.LogUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ChenXS
 * 系统级用户定时任务
 */
@Component
public class SysUserTask {

    /**
     * 开启一个定时任务
     * 程序启动后3秒开始执行，每10秒执行一次
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 10000)
    public void task() {
        LogUtil.info("定时任务执行....");
    }
}
