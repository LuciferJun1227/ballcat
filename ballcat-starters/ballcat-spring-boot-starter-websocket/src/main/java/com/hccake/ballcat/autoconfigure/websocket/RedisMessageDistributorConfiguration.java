package com.hccake.ballcat.autoconfigure.websocket;

import com.hccake.ballcat.common.websocket.distribute.MessageDistributor;
import com.hccake.ballcat.common.websocket.distribute.RedisMessageDistributor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.annotation.PostConstruct;

/**
 * 基于 Redis Pub/Sub 的消息分发器
 *
 * @author hccake
 */
@ConditionalOnClass(StringRedisTemplate.class)
@ConditionalOnProperty(prefix = WebSocketProperties.PREFIX, name = "message-distributor",
		havingValue = MessageDistributorTypeConstants.REDIS)
@Configuration(proxyBeanMethods = false)
public class RedisMessageDistributorConfiguration {

	@Bean
	@ConditionalOnMissingBean(MessageDistributor.class)
	public RedisMessageDistributor messageDistributor(StringRedisTemplate stringRedisTemplate) {
		return new RedisMessageDistributor(stringRedisTemplate);
	}

	@Bean
	@ConditionalOnBean(RedisMessageDistributor.class)
	@ConditionalOnMissingBean
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		return container;
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnMissingBean(MessageDistributor.class)
	@RequiredArgsConstructor
	static class RedisMessageListenerRegisterConfiguration {

		private final RedisMessageListenerContainer redisMessageListenerContainer;

		private final RedisMessageDistributor redisWebsocketMessageListener;

		@PostConstruct
		public void addMessageListener() {
			redisMessageListenerContainer.addMessageListener(redisWebsocketMessageListener,
					new PatternTopic(RedisMessageDistributor.CHANNEL));
		}

	}

}