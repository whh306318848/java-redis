package com.kuang.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: wuhaohua
 * @date: Created in 2022/2/19 21:39
 * @description: TODO
 */
@Component
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

    /***
     * @param key 键名
     * @param delta 递增因子（必须大于0）
     * @return {@link {@link long}} value自增之后的值
     * @throws RuntimeException 自增因子小于等于0时，抛出运行时异常
     * @Description: 自增
     **/
    public long incr(String key, long delta) {
        if (delta <= 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /***
     * @param key 键名
     * @param delta 递减因子（必须大于0）
     * @return {@link {@link long}} value自减之后的值
     * @throws RuntimeException 自增因子小于等于0时，抛出运行时异常
     * @Description: 自减
     **/
    public long decr(String key, long delta) {
        if (delta <= 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    // ==================== Hash操作 ===============================

    /***
     * @param key 键名
     * @return {@link {@link java.util.Map<java.lang.Object,java.lang.Object>}} 对应的多个键值
     * @Description: 获取hashKey对应的所有键值
     **/
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /***
     * @param key 键名
     * @param map 对应的多个键值
     * @return {@link {@link boolean}} 当设置成功时返回true，失败返回false
     * @Description: 向key中保存多个键值
     **/
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * @param key 键名
     * @param map 对应的多个键值
     * @param time 失效时间（秒），必须大于0
     * @return {@link {@link boolean}} 当设置成功时返回true，失败返回false
     * @Description: 向key中保存多个键值，并设置失效时间
     **/
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * @param key 键名
     * @param item 项
     * @param value 值
     * @return {@link {@link boolean}} 保存成功返回true， 失败返回false
     * @Description: 向一张hash表中放入数据，如果不存在则创建
     **/
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * @param key 键名
     * @param item 项
     * @param value 值
     * @param time 失效时间（秒），必须大于0
     * @return {@link {@link boolean}}
     * @Description: 向一张hash表中放入数据，如果不存在则创建，并设置失效时间
     **/
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * @param key 键名
     * @param item 项，可以是一个，也可以是多个
     * @Description: 删除hash表中的值
     **/
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /***
     * @param key 键名
     * @param item 项
     * @return {@link {@link boolean}} 如果有该项的值则返回true，否则返回false
     * @Description: 判断hash表中是否有该项的值
     **/
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /***
     * @param key 键名
     * @param item 项
     * @param by 自增因子（必须大于0）
     * @return {@link {@link double}} 自增之后的结果
     * @Description: hash递增 如果不存在，就会创建一个
     **/
    public double hincr(String key, String item, double by) {
        if (by <= 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /***
     * @param key 键名
     * @param item 项
     * @param by 自减因子（必须大于0）
     * @return {@link {@link double}} 自减之后的结果
     * @Description: hash递增 如果不存在，就会创建一个
     **/
    public double hdecr(String key, String item, double by) {
        if (by <= 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ==================== Set操作 ===============================

    /***
     * @param key 键名
     * @return {@link {@link java.util.Set<java.lang.Object>}} key对应的set的值
     * @Description: 根据key获取Set的所有值
     **/
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /***
     * @param key 键名
     * @param value 要查询的值值
     * @return {@link {@link boolean}} 若存在则返回true，否则返回false
     * @Description: 根据value从Set中查询是否存在
     **/
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * @param key 键名
     * @param values 要放入的Set数据，可以是一个，也可以是多个
     * @return {@link {@link long}} 本次放入set成功的个数
     * @Description: 将数据放入Set缓存中
     **/
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /***
     * @param key 键名
     * @param time 失效时间（秒），必须大于0
     * @param values 要放入的Set数据，可以是一个，也可以是多个
     * @return {@link {@link long}} 本次放入set成功的个数
     * @Description: 将数据放入Set缓存中，并设置失效时间
     **/
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /***
     * @param key 键名
     * @return {@link {@link long}} key对应的Set的个数
     * @Description: 获取Set缓存的长度
     **/
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /***
     * @param key 键名
     * @param values 要移除的Set，可以是一个，也可以是多个
     * @return {@link {@link long}} 移除的个数
     * @Description: 移除值为value的Set
     **/
    public long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    // ==================== List操作 ===============================

    /***
     * @param key 键名
     * @param start 开始为止
     * @param end 结束为止
     * @return {@link {@link java.util.List<java.lang.Object>}} List缓存的内容
     * @Description: 获取List缓存的内容
     **/
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /***
     * @param key 键
     * @return {@link {@link long}} 对应List的长度
     * @Description: 获取List缓存的长度
     **/
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /***
     * @param key 键名
     * @param index 索引
     * @return {@link {@link java.lang.Object}} 索引对应的值
     * @Description: 通过索引获取List中的值
     **/
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /***
     * @param key 键名
     * @param value 需要放入的值
     * @return {@link {@link boolean}} 保存成功返回true，失败返回false
     * @Description: 将值放入list缓存
     **/
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * @param key 键名
     * @param value 需要放入的值
     * @param time 失效时间（秒）， 必须大于0
     * @return {@link {@link boolean}} 保存成功返回true，失败返回false
     * @Description: 将值放入list缓存，并设置失效时间
     **/
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * @param key 键名
     * @param value 要放入缓存的list
     * @return {@link {@link boolean}} 保存成功返回true，失败返回false
     * @Description: 将list放入list缓存
     **/
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * @param key 键名
     * @param value 要放入缓存的list
     * @param time 失效时间（秒），必须大于0
     * @return {@link {@link boolean}} 保存成功返回true，失败返回false
     * @Description: 将list放入list缓存，并设置失效时间
     **/
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key 键名
     * @param index 索引值
     * @param value 修改的目标值
     * @return {@link Boolean} 修改成功返回true，失败返回false
     **/
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 从list中移除count个value值
     *
     * @param key   键名
     * @param count 想要移除的个数
     * @param value 要移除的值
     * @return {@link Long} 成功移除的个数
     **/
    public long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除并获取列表中的（左边）第一个元素
     * @param key 键名
     * @return {@link Object} 移除的元素
     * @date: 2022/2/20
    **/
    public Object lLeftPop(String key) {
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


}
