package com.cxs.core.config.shiro;

import com.cxs.core.utils.LogUtil;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author ChenXS
 * 重写 shiro getSessionId 方法，解决前后端分离后session问题
 */
public class SessionManager extends DefaultWebSessionManager {

    @Override
    public Serializable getSessionId(SessionKey key) {
        Serializable id = super.getSessionId(key);
        if (id == null && WebUtils.isWeb(key)) {
            ServletRequest request = WebUtils.getRequest(key);
            ServletResponse response = WebUtils.getResponse(key);
            id = this.getSessionId(request, response);
        }

        return id;
    }

    /**
     * 重写获取sessionId的方法调用当前Manager的获取方法
     *
     * @param request  请求信息
     * @param response 返回信息
     * @return Serializable
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        return this.getReferencedSessionIdOwner(request, response);
    }

    private Serializable getReferencedSessionIdOwner(ServletRequest request, ServletResponse response) {
        String id = this.getSessionIdCookieValueOwner(request, response);
        if (id != null) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "cookie");
        } else {
            id = this.getUriPathSegmentParamValueOwner(request);
            if (id == null) {
                // 获取请求头中的session
                String authorization = "Authorization";
                id = WebUtils.toHttp(request).getHeader(authorization);
                if (id == null) {
                    String name = this.getSessionIdNameOwner();
                    id = request.getParameter(name);
                    if (id == null) {
                        id = request.getParameter(name.toLowerCase());
                    }
                }
            }
            if (id != null) {
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "url");
            }
        }
        if (id != null) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        }
        return id;
    }

    // copy super
    private String getSessionIdCookieValueOwner(ServletRequest request, ServletResponse response) {
        if (!this.isSessionIdCookieEnabled()) {
            LogUtil.debug("Session ID cookie is disabled - session id will not be acquired from a request cookie.");
            return null;
        } else if (!(request instanceof HttpServletRequest)) {
            LogUtil.debug("Current request is not an HttpServletRequest - cannot get session ID cookie.  Returning null.");
            return null;
        } else {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            return this.getSessionIdCookie().readValue(httpRequest, WebUtils.toHttp(response));
        }
    }

    // copy adn refactor super's method
    private String getUriPathSegmentParamValueOwner(ServletRequest servletRequest) {
        if (!(servletRequest instanceof HttpServletRequest)) {
            return null;
        } else {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String uri = request.getRequestURI();
            if (uri == null) {
                return null;
            } else {
                return chackUri(uri);
            }
        }
    }

    private String chackUri(String uri) {
        int queryStartIndex = uri.indexOf(63);
        if (queryStartIndex >= 0) {
            uri = uri.substring(0, queryStartIndex);
        }
        int index = uri.indexOf(59);
        if (index < 0) {
            return null;
        } else {
            String token = "JSESSIONID" + "=";
            uri = uri.substring(index + 1);
            index = uri.lastIndexOf(token);
            if (index < 0) {
                return null;
            } else {
                uri = uri.substring(index + token.length());
                index = uri.indexOf(59);
                if (index >= 0) {
                    uri = uri.substring(0, index);
                }
                return uri;
            }
        }
    }

    // copy super
    private String getSessionIdNameOwner() {
        String name = this.getSessionIdCookie() != null ? this.getSessionIdCookie().getName() : null;
        if (name == null) {
            name = "JSESSIONID";
        }
        return name;
    }

}
