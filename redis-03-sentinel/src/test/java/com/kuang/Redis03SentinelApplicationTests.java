package com.kuang;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuang.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class Redis03SentinelApplicationTests {

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate redisTemplate;

	@Test
	void contextLoads() throws JsonProcessingException {
//		// 获取当前连接的服务器信息
//		try {
//			RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
//			RedisConnection connection = RedisConnectionUtils.getConnection(factory);
//			System.out.println(connection.info());
//		}catch (Exception ex) {
//			ex.printStackTrace();
//		}
		redisTemplate.opsForValue().set("k1", "v1");
		System.out.println(redisTemplate.opsForValue().get("k1"));

		User user = new User("张三", 18);
		String jsonUser = new ObjectMapper().writeValueAsString(user);
		redisTemplate.opsForValue().set("user", jsonUser);
		System.out.println(redisTemplate.opsForValue().get("user"));
	}

}
