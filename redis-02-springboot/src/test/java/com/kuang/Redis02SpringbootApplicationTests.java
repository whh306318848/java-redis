package com.kuang;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuang.pojo.User;
import com.kuang.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class Redis02SpringbootApplicationTests {

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate redisTemplate;

	@Autowired
	private RedisUtils redisUtils;

	@Test
	void contextLoads() {

		// 在企业开发中，我们80%的情况下，都不会使用这个原生的方式去编写代码！
		// RedisUtils

		// redisTemplate	操作不同的数据类型，api和我们的指令是一样的
		// opsForValue	操作字符串	类似String
		// opsForList	操作List		类似List
		// opsForSet	操作Set
		// opsForHash	操作Hash
		// opsForZSet	操作ZSet
		// opsForGeo	操作geospatial

		// 除了基本的操作，我们常用的方法都可以通过redisTemplate操作，比如实物，和基本的

		// 获取redis的连接对象
//		RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
//		connection.flushDb();
//		connection.flushAll();

		redisTemplate.opsForValue().set("mykey", "kuangshen");
		System.out.println(redisTemplate.opsForValue().get("mykey"));
	}

	@Test
	public void test() throws JsonProcessingException {
		// 真实的开发，一般都使用json来传递对象
		User user = new User("狂神说", 3);
		String jsonUser = new ObjectMapper().writeValueAsString(user);
		redisTemplate.opsForValue().set("user", user);
		System.out.println(redisTemplate.opsForValue().get("user"));
	}

	@Test
	public void test2() {
//		redisUtils.set("age", 1);
//		redisUtils.decr("age", 2);
//		System.out.println(redisUtils.incr("age", 1));

		Set<String> set = new HashSet<>();
		set.add("c");
		set.add("d");
		System.out.println(redisUtils.sSet("set", set));
		redisUtils.lLeftPop("a");
	}
}
