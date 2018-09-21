package com.cxs.core.config.WebMvc;

import com.cxs.core.utils.PropertiesUtil;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/*", filterName = "cors")
public class Cors implements Filter {
    Properties properties = null;

    @Override
    public void init(FilterConfig config) {
        // 读取配置文件内容
        properties = PropertiesUtil.getPropertiesContext("config" + File.separator + "cors.properties");
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        for (Object key : properties.keySet()) {
            response.setHeader(key.toString(), properties.getProperty(key.toString()));
        }
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(200);
            return;
        }
        filterChain.doFilter(servletRequest, response);
    }
}
