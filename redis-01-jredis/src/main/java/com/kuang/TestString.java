package com.kuang;

import redis.clients.jedis.Jedis;

/**
 * @author: wuhaohua
 * @date: Created in 2022/2/14 21:52
 * @description: TODO
 */
public class TestString {
    public static void main(String[] args) {
        // 连接redis服务器并输入密码验证
        Jedis jedis = new Jedis("192.168.76.128", 6379);
        jedis.auth("123456");

        jedis.flushDB();
        System.out.println("==========增加数据==========");
        System.out.println(jedis.set("key1", "value1"));
        System.out.println(jedis.set("key2", "value2"));
        System.out.println(jedis.set("key3", "value3"));
        System.out.println("删除键key2：" + jedis.del("key2"));
        System.out.println("获取键key2：" + jedis.get("key2"));
        System.out.println("修改key1：" + jedis.set("key1", "value1Changed"));
        System.out.println("获取key1的值：" + jedis.get("key1"));
        System.out.println("在key3后面加入值："+jedis.append("key3", "End"));
        System.out.println("key3的值："+jedis.get("key3"));
        System.out.println("增加多个键值对："+jedis.mset("key01", "value01", "key02", "value02", "key03", "value03"));
        System.out.println("获取多个键值对："+jedis.mget("key01", "key02", "key03"));
        System.out.println("获取多个键值对："+jedis.mget("key01", "key02", "key03", "key04"));
        System.out.println("删除多个键值对："+jedis.del("key01", "key02"));
        System.out.println("获取多个键值对："+jedis.mget("key01", "key02", "key03"));

        jedis.flushDB();
        System.out.println("==========新增键值对防止覆盖原先值==========");
        System.out.println(jedis.setnx("key1", "value1"));
        System.out.println(jedis.setnx("key2", "value2"));
        System.out.println(jedis.setnx("key2", "value2-new"));
        System.out.println(jedis.get("key1"));
        System.out.println(jedis.get("key2"));

        System.out.println("==========新增键值并设置有效时间==========");
        System.out.println(jedis.setex("key3", 2, "value3"));
        System.out.println(jedis.get("key3"));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(jedis.get("key3"));

        System.out.println("==========获取原值，更新为新值==========");
        System.out.println(jedis.getSet("key2", "key2GetSet"));
        System.out.println(jedis.get("key2"));

        System.out.println("获取key2的值的字符串："+jedis.getrange("key2", 2, 4));
    }
}
