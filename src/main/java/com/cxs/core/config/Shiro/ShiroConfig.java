package com.cxs.core.config.Shiro;

import com.cxs.core.utils.LogUtil;
import com.cxs.core.utils.PropertiesUtil;
import com.cxs.core.utils.RedisUtil;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShiroConfig {

    private final RedisUtil redisUtil;

    @Autowired
    public ShiroConfig(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // setLoginUrl 如果不设置值，默认会自动寻找Web工程根目录下的"/login.jsp"页面 或 "/login" 映射
        shiroFilterFactoryBean.setLoginUrl("/notLogin");
        // 设置无权限时跳转的 url;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        // 设置拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 读取配置文件内容
        Properties properties = PropertiesUtil.getPropertiesContext("config" + File.separator + "shiro.properties");
        for (Object key : properties.keySet()) {
            filterChainDefinitionMap.put(key.toString(), properties.getProperty(key.toString()));
        }
        //必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        LogUtil.info("[Shiro]: 拦截器工厂类注入成功");
        return shiroFilterFactoryBean;
    }

    /**
     * 注入 securityManager
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(myRealm());
        //自定义session管理
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * 自定义SessionManager
     *
     * @return SessionManager
     */
    @Bean
    public SessionManager sessionManager() {
        SessionManager shiroSessionManager = new SessionManager();
        // 设置session过期时间
        shiroSessionManager.setGlobalSessionTimeout(1800000);
        // 设置session管理
        shiroSessionManager.setSessionDAO(new SessionRedisDao(redisUtil));
        return shiroSessionManager;
    }

    /**
     * 自定义身份认证 realm;
     * <p>
     * 必须写这个类，并加上 @Bean 注解，目的是注入 MyRealm，
     * 否则会影响 MyRealm类 中其他类的依赖注入
     */
    @Bean
    public MyRealm myRealm() {
        return new MyRealm();
    }
}