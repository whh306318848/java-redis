package com.kuang;

import redis.clients.jedis.Jedis;

/**
 * @author: wuhaohua
 * @date: Created in 2022/2/15 0:00
 * @description: TODO
 */
public class TestSet {
    public static void main(String[] args) {
        // 连接redis服务器并输入密码验证
        Jedis jedis = new Jedis("192.168.76.128", 6379);
        jedis.auth("123456");

        jedis.flushDB();
        System.out.println("==========向集合中添加元素（不重复）==========");
        System.out.println(jedis.sadd("eleSet", "e1", "e2", "e4", "e3", "e0", "e8", "e7", "e5"));
        System.out.println(jedis.sadd("eleSet", "e6"));
        System.out.println(jedis.sadd("eleSrt", "e6"));
        System.out.println("eleSet的所有元素：" + jedis.smembers("eleSet"));
        System.out.println("删除一个元素e0：" + jedis.srem("eleSet", "e0"));
        System.out.println("eleSet的所有元素：" + jedis.smembers("eleSet"));
        System.out.println("删除两个元素e7和e6：" + jedis.srem("eleSet", "e7", "e6"));
        System.out.println("eleSet的所有元素：" + jedis.smembers("eleSet"));
        System.out.println("随机的移除集合中的一个元素：" + jedis.spop("eleSet"));
        System.out.println("随机的移除集合中的一个元素：" + jedis.spop("eleSet"));
        System.out.println("eleSet的所有元素：" + jedis.smembers("eleSet"));
        System.out.println("eleSet中包含的元素个数：" + jedis.scard("eleSet"));
        System.out.println("e3是否在eleSet中：" + jedis.sismember("eleSet", "e3"));
        System.out.println("e1是否在eleSet中：" + jedis.sismember("eleSet", "e1"));
        System.out.println("e5是否在eleSet中：" + jedis.sismember("eleSet", "e5"));

        System.out.println("===============================");
        System.out.println(jedis.sadd("eleSet1", "e1", "e2", "e4", "e3", "e0", "e8", "e7", "e5"));
        System.out.println(jedis.sadd("eleSet2", "e1", "e2", "e4", "e3", "e0", "e8"));
        System.out.println("将eleSet1中删除e1，并存入eleSet3中：" + jedis.smove("eleSet1", "eleSet3", "e1"));
        System.out.println("将eleSet2中删除e2，并存入eleSet3中：" + jedis.smove("eleSet2", "eleSet3", "e2"));
        System.out.println("eleSet1中的元素：" + jedis.smembers("eleSet1"));
        System.out.println("eleSet3中的元素：" + jedis.smembers("eleSet3"));

        System.out.println("==========集合运算==========");
        System.out.println("eleSet1中的元素：" + jedis.smembers("eleSet1"));
        System.out.println("eleSet2中的元素：" + jedis.smembers("eleSet2"));
        System.out.println("eleSet1和eleSet2的交集：" + jedis.sinter("eleSet1", "eleSet2"));
        System.out.println("eleSet1和eleSet2的并集：" + jedis.sunion("eleSet1", "eleSet2"));
        System.out.println("eleSet1和eleSet2的差集：" + jedis.sdiff("eleSet1", "eleSet2"));// eleSet1中有，eleSet2中没有的元素
        // 求交集并将交集保存到eleSet4的集合中
        jedis.sinterstore("eleSet4", "eleSet1", "eleSet2");
        System.out.println("eleSet4中的元素：" + jedis.smembers("eleSet4"));
    }
}
