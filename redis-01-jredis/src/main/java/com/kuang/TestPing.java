package com.kuang;

import redis.clients.jedis.Jedis;

/**
 * @author: wuhaohua
 * @date: Created in 2022/2/7 23:28
 * @description: TODO
 */
public class TestPing {
    public static void main(String[] args) {
        // 1、new Jedis 对象即可
        Jedis jedis = new Jedis("192.168.76.128", 6379);
        // 验证redis的密码
        jedis.auth("123456");
        // Jedis 所有的命令就是我们之前学习的所有指令！之前的所有指令在这里就是一个个的方法。

        System.out.printf(jedis.ping());
    }
}
