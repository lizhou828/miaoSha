package com.liz.distributeLock.miaoSha.demo1.service;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2019年05月06日 15时27分
 */
public interface JedisClientIService {

    /* key-value数据类型操作*/
    public void set(String key, String value);

    /**
     * @param key
     * @param value
     * @param timeOut  默认过期时间的单位：秒
     */
    public void set(String key, Object value, long timeOut);

    public void set(String key, Object value, long timeOut, TimeUnit unit);

    public String get(String key);

    public void setObject(String key, Object object);

    public <T> T getObject(String key, Class<T> clz);



    /*  通用操作  */
    public boolean delete(String key);

    public boolean hasKey(String key);



    /*  Hash 数据类型操作  */
    public boolean hSet(String key, String field, String value);

    public Map<Object, Object> hGetAll(String key);

    public Object hGet(String key, String field);

    public void hDel(String key, String... fields);



    /*  set数据类型操作  */
    Long sadd(String key, Object... values);

    Long srem(String key, Object... values);

    /**
     * Get size of set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/scard">Redis Documentation: SCARD</a>
     */
    Long scard(String key);

    /**
     * Check if set at {@code key} contains {@code value}.
     *
     * @param key must not be {@literal null}.
     * @param o
     * @return
     * @see <a href="http://redis.io/commands/sismember">Redis Documentation: SISMEMBER</a>
     */
    Boolean sIsMember(String key, Object o);

    Set sMembers(String key);

    /**
     * 根据key获取过期时间
     * @param key
     * @return
     */
    Long getExpire(String key);

    void setStringValue(String key, String str, long timeOut, TimeUnit unit);

    /**
     * 给指定key设置生存时间
     * @param key key
     * @param lifetime 生存时间
     * @param unit 单位
     * @return
     */
    boolean setKeyLifeTime(String key, long lifetime, TimeUnit unit);
}
