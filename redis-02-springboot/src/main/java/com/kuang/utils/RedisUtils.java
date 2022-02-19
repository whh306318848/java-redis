package com.kuang.utils;

import javafx.scene.control.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author: wuhaohua
 * @date: Created in 2022/2/19 21:39
 * @description: TODO
 */
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /***
     * @param key 键名
     * @param time 失效时间（秒）
     * @return {@link {@link boolean}} 设置成功返回true，失败返回false
     * @Description: 指定缓存失效时间
     **/
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * @param key 键名
     * @return {@link {@link long}} 时间（秒），返回0代表为永久有效
     * @Description: 根据key获取过期时间
     **/
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /***
     * @param key 键名
     * @return {@link {@link boolean}} 若存在则返回true，若不存在则返回false
     * @Description: 判断key是否存在
     **/
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * @param key 键名，可以传一个值，也可以传多个
     * @Description: 删除缓存
     **/
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    // ==================== String操作 ===============================

    /***
     * @param key 键名
     * @return {@link {@link java.lang.Object}} 值
     * @Description: 获取缓存
     **/
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /***
     * @param key 键名
     * @param value 值
     * @return {@link {@link boolean}} 保存成功返回true，保存失败返回false
     * @Description: 设置缓存
     **/
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * @param key 键名
     * @param value 值
     * @param time 失效时间（秒），若time小于等于0，则不会失效
     * @return {@link {@link boolean}} 保存成功返回true，保存失败返回false
     * @Description: 设置缓存并设置失效时间
     **/
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
