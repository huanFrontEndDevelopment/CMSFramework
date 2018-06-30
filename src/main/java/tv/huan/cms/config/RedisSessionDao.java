package tv.huan.cms.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Project Name:BasicCMS
 * File Name:RedisSessionDao
 *
 * @author wangyuxi
 * @date 2018/6/7 上午10:22
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Slf4j
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RedisSessionDao extends AbstractSessionDAO {

    /**
     * Session超时时间，单位为毫秒
     */
    @Getter
    @Setter
    private long expireTime = 120000;
    /**
     * Redis操作类，对这个使用不熟悉的，可以参考前面的博客
     */
    @Autowired
    @Getter
    @Setter
    private RedisTemplate redisTemplate;

    public RedisSessionDao() {
        super();
    }

    public RedisSessionDao(long expireTime, RedisTemplate redisTemplate) {
        super();
        this.expireTime = expireTime;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 更新session
     * @param session session
     * @throws UnknownSessionException
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        log.debug("===============update================");
        if (session == null || session.getId() == null) {
            return;
        }
        session.setTimeout(expireTime);
        redisTemplate.opsForValue().set(session.getId(), session, expireTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 删除session
     * @param session Session
     */
    @Override
    public void delete(Session session) {
        log.debug("===============delete================");
        if (null == session) {
            return;
        }
        redisTemplate.opsForValue().getOperations().delete(session.getId());
    }

    /**
     * 获取活跃的session，可以用来统计在线人数，如果要实现这个功能，可以在将session加入redis时指定一个session前缀，统计的时候则使用keys("session-prefix*")的方式来模糊查找redis中所有的session集合
     * @return Collection<Session>
     */
    @Override
    public Collection<Session> getActiveSessions() {
        log.debug("==============getActiveSessions=================");
        return redisTemplate.keys("*");
    }

    /**
     * 加入session
     * @param session Session
     * @return Serializable
     */
    @Override
    protected Serializable doCreate(Session session) {
        log.debug("===============doCreate================");
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);

        redisTemplate.opsForValue().set(session.getId(), session, expireTime, TimeUnit.MILLISECONDS);
        return sessionId;
    }

    /**
     * 读取session
     * @param sessionId sessionId
     * @return Session
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        log.debug("==============doReadSession=================");
        if (sessionId == null) {
            return null;
        }
        return (Session) redisTemplate.opsForValue().get(sessionId);
    }




}


