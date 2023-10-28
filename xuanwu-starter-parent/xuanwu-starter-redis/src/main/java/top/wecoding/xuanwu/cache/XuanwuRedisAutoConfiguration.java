package top.wecoding.xuanwu.cache;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import top.wecoding.xuanwu.cache.redis.RedisHelper;

/**
 * @author wecoding
 * @since 0.9
 */
@AutoConfiguration
@ConditionalOnClass(RedisConnectionFactory.class)
public class XuanwuRedisAutoConfiguration {

	/**
	 * @return StringRedisTemplate
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		// key 序列化
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		// value 序列化
		template.setValueSerializer(RedisSerializer.json());
		template.setHashValueSerializer(RedisSerializer.json());
		template.afterPropertiesSet();
		return template;
	}

	/**
	 * @return StringRedisTemplate
	 */
	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate redisTemplate = new StringRedisTemplate();
		buildRedisTemplate(redisTemplate, redisConnectionFactory);
		return redisTemplate;
	}

	private void buildRedisTemplate(RedisTemplate<String, String> redisTemplate,
			RedisConnectionFactory redisConnectionFactory) {
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setStringSerializer(stringRedisSerializer);
		redisTemplate.setDefaultSerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		redisTemplate.setHashValueSerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(stringRedisSerializer);
		redisTemplate.setConnectionFactory(redisConnectionFactory);
	}

	/**
	 * @return Hash 处理类
	 */
	@Bean
	public HashOperations<String, String, String> hashOperations(StringRedisTemplate redisTemplate) {
		return redisTemplate.opsForHash();
	}

	/**
	 * @return String 处理类
	 */
	@Bean
	public ValueOperations<String, String> valueOperations(StringRedisTemplate redisTemplate) {
		return redisTemplate.opsForValue();
	}

	/**
	 * @return List 处理类
	 */
	@Bean
	public ListOperations<String, String> listOperations(StringRedisTemplate redisTemplate) {
		return redisTemplate.opsForList();
	}

	/**
	 * @return Set 处理类
	 */
	@Bean
	public SetOperations<String, String> setOperations(StringRedisTemplate redisTemplate) {
		return redisTemplate.opsForSet();
	}

	/**
	 * @return ZSet 处理类
	 */
	@Bean
	public ZSetOperations<String, String> zSetOperations(StringRedisTemplate redisTemplate) {
		return redisTemplate.opsForZSet();
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisHelper redisHelper(RedisTemplate<String, Object> redisTemplate) {
		return new RedisHelper(redisTemplate);
	}

}
