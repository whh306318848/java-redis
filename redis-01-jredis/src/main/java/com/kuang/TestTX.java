package com.kuang;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * @author: wuhaohua
 * @date: Created in 2022/2/15 23:16
 * @description: TODO
 */
public class TestTX {
    public static void main(String[] args) {
        // 连接redis服务器并输入密码验证
        Jedis jedis = new Jedis("192.168.76.128", 6379);
        jedis.auth("123456");

        jedis.flushDB();

        // 准备数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", "world");
        jsonObject.put("name", "xiaoming");
        String result = jsonObject.toJSONString();

        // 开启事务
        Transaction multi = jedis.multi();
//        jedis.watch(result);
        try {
            multi.set("user1", result);
            multi.set("user2", result);

            // 代码抛出异常，事务执行失败
            int i = 1 / 0;

            // 执行事务
            multi.exec();
        } catch (Exception ex) {
            // 放弃事务
            multi.discard();
            ex.printStackTrace();
        } finally {
            // 查询数据
            System.out.println("user1的信息：" + jedis.get("user1"));
            System.out.println("user2的信息：" + jedis.get("user2"));

            // 关闭连接
            jedis.close();
        }
    }
}
