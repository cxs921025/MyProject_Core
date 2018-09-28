package com.cxs.core.config.Shiro;

import com.cxs.core.utils.RedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

import java.io.Serializable;

/**
 * @author ChenXS
 * 通过redis实现shiro session共享
 */
public class SessionRedisDao extends EnterpriseCacheSessionDAO {

    private RedisUtil redisUtil;

    public SessionRedisDao(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    // 更新session
    @Override
    protected void doUpdate(Session session) {
        super.doUpdate(session);
        redisUtil.setObject(session.getId().toString().getBytes(), redisUtil.objectToByteArray(session), 30 * 60);
    }

    // 删除session
    @Override
    protected void doDelete(Session session) {
        super.doDelete(session);
        redisUtil.deleteByKey(session.getId().toString());
    }

    // 创建session，并存储到redis中
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        redisUtil.setObject(session.getId().toString().getBytes(), redisUtil.objectToByteArray(session), 30 * 60);
        return sessionId;
    }

    // 获取session
    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session = super.doReadSession(sessionId);
        if (session == null) {
            byte[] bytes = redisUtil.getObject(sessionId.toString().getBytes());
            if (bytes != null && bytes.length > 0) {
                session = (Session) redisUtil.byteArrayToObject(bytes);
            }
        }
        return session;
    }
}
