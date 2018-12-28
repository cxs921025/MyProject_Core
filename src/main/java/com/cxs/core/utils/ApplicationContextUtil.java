package com.cxs.core.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@SuppressWarnings({"unchecked", "unused"})
@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 通过类名 获取Bean
     *
     * @param name 类名
     * @return Bean
     */
    public Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 通过Class 获取Bean
     *
     * @param clazz Class
     * @param <T>   T
     * @return Bean
     */
    public <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 通过类名和Class 获取Bean
     *
     * @param name  类名
     * @param clazz Class
     * @param <T>   T
     * @return Bean
     */
    public <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }
}
