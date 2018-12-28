package com.cxs.core.config.webmvc;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/*", filterName = "cors")
public class Cors implements Filter {

    @Override
    public void destroy() {
        // Do nothing
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // 允许任何域名访问
        String origin = request.getHeader("Origin");
        if (StringUtils.isNotBlank(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        // 允许请求所携带的任何自定义请求头信息
        String headers = request.getHeader("Access-Control-Request-Headers");
        if (StringUtils.isNotEmpty(headers)) {
            response.setHeader("Access-Control-Allow-Headers", headers);
        }
        // 允许浏览器携带用户身份信息（cookie）
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 允许浏览器缓存预检命令的时间，单位是秒
        response.setHeader("Access-Control-Max-Age", "1800");
        // 允许请求的方法
        response.setHeader("Access-Control-Allow-Methods", "*");

        // 如果是预检命令，直接返回正确状态
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(200);
            return;
        }
        filterChain.doFilter(servletRequest, response);
    }
}
