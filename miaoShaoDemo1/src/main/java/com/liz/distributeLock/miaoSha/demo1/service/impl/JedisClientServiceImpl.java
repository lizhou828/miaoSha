/*
 *Project: common
 *File: com.diyun.dubbo.common.api.service.JedisClient.java <2019年05月06日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.liz.distributeLock.miaoSha.demo1.service.impl;

import com.alibaba.fastjson.JSON;
import com.liz.distributeLock.miaoSha.demo1.service.JedisClientIService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
/**
 * @author lizhou
 * @version 1.0
 * @Date 2019年05月06日 15时26分
 */


/**
 *
 * @author liz
 * @version 1.0
 */
@Service("jedisClientIService")
public class JedisClientServiceImpl implements JedisClientIService {
    private static final Logger log = LogManager.getLogger(JedisClientServiceImpl.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void set(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
        } catch (Exception ex) {
            log.error("set error.", ex);
        }
    }

    /**
     *
     * @param key
     * @param value
     * @param timeOut  默认单位：秒
     */
    @Override
    public void set(String key, Object value, long timeOut) {
        set(key,value,timeOut,TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object object, long timeOut, TimeUnit unit) {
        try {
            String value = JSON.toJSONString(object);
            stringRedisTemplate.opsForValue().set(key, value,timeOut,unit);
        } catch (Exception ex) {
            log.error("set error:" + ex.getMessage(), ex);
        }
    }

    @Override
    public String get(String key) {
        String result = null;
        try {
            result = stringRedisTemplate.opsForValue().get(key);
        } catch (Exception ex) {
            log.error("get error.", ex);
        }
        return result;
    }


    @Override
    public void setObject(String key, Object object) {
        String value = JSON.toJSONString(object);
        this.set(key,value);
    }

    @Override
    public <T> T getObject(String key, Class<T> clz) {
        String json = this.get(key);
        if(!StringUtils.isEmpty(json)){
            T t  = JSON.parseObject(json, clz);
            return t;
        }
        return null;
    }

    @Override
    public boolean delete(String key) {
        if(StringUtils.isEmpty(key)){
            return false;
        }
        try{
            stringRedisTemplate.delete(key);
            return true;
        }catch (Exception e){
            log.error("delete error:" + e.getMessage(), e);
            return false;
        }

    }

    @Override
    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }


    @Override
    public boolean hSet(String key, String field, String value) {
        try{
            redisTemplate.opsForHash().put(key,field,value);
            return true;
        }catch (Exception e){
            log.error("redisTemplate.opsForHash().put("+key+","+field+" ,"+value+") failed:" + e.getMessage(),e);
            return false;
        }
    }

    @Override
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public void hDel(String key, String... fields) {
        redisTemplate.opsForHash().delete(key,fields);
    }

    /*  set数据类型操作  */
    /**
     * Add given {@code values} to set at {@code key}.
     * @param key
     * @param values
     * @return
     */
    @Override
    public Long sadd(String key, Object... values){
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * Remove given {@code values} from set at {@code key} and return the number of removed elements.
     * @param key
     * @param values
     * @return
     */
    @Override
    public Long srem(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     *  Get size of set at {@code key}.
     * @param key must not be {@literal null}.
     * @return
     */
    @Override
    public Long scard(String key) {
        return redisTemplate.opsForSet().size(key);
    }


    /**
     * Check if set at {@code key} contains {@code value}.
     *
     * @param key must not be {@literal null}.
     * @param value
     * @return
     * @see <a href="http://redis.io/commands/sismember">Redis Documentation: SISMEMBER</a>
     */
    @Override
    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public Set sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }


    @Override
    public void setStringValue(String key, String str, long timeOut, TimeUnit unit) {
        try {
            stringRedisTemplate.opsForValue().set(key, str,timeOut,unit);
        } catch (Exception ex) {
            log.error("set error:" + ex.getMessage(), ex);
        }
    }

    /**
     * 给指定key设置生存时间
     * @param key
     * @param lifetime
     * @param unit
     * @return
     */
    public boolean setKeyLifeTime(String key,long lifetime,TimeUnit unit){
        boolean result=redisTemplate.expire(key, lifetime, unit);
        return result;
    }

}
