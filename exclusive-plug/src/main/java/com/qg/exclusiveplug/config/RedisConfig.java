package com.qg.exclusiveplug.config;

import com.qg.exclusiveplug.model.User;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        //user信息缓存配置
        RedisCacheConfiguration userCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)).disableCachingNullValues().prefixKeysWith("role");
        //product信息缓存配置
        RedisCacheConfiguration productCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)).disableCachingNullValues().prefixKeysWith("test");
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        redisCacheConfigurationMap.put("role", userCacheConfiguration);
        redisCacheConfigurationMap.put("test", productCacheConfiguration);
        //初始化一个RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        //设置CacheManager的值序列化方式为JdkSerializationRedisSerializer,但其实RedisCacheConfiguration默认就是使用StringRedisSerializer序列化key，JdkSerializationRedisSerializer序列化value,所以以下注释代码为默认实现
        //ClassLoader loader = this.getClass().getClassLoader();
        //JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
        //RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializer);
        //RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);

        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        //设置默认超过期时间是30秒
        defaultCacheConfig.entryTtl(Duration.ofSeconds(30));
        //初始化RedisCacheManager
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig, redisCacheConfigurationMap);
        return cacheManager;
    }

    @Bean
    <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        System.out.println("加载redis配置");

        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer(User.class.getClassLoader());
//        Jackson2JsonRedisSerializer j = new Jackson2JsonRedisSerializer(User.class);
        // value值得序列化采用fastJsonRedisSerializer
        redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);
        // key的序列化采用StringRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }

    /**
     * 设置spring session redis 序列化方式
     * @param factory
     * @return
     */
    /*@Bean
    public SessionRepository sessionRepository(RedisConnectionFactory factory){
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        Jackson2JsonRedisSerializer j = new Jackson2JsonRedisSerializer(Object.class);
        redisTemplate.setValueSerializer(j);
        redisTemplate.setHashValueSerializer(j);
        redisTemplate.setConnectionFactory(factory);
        RedisOperationsSessionRepository sessionRepository =  new RedisOperationsSessionRepository(redisTemplate);

        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        sessionRepository.setDefaultSerializer(fastJsonRedisSerializer);
        sessionRepository.setDefaultMaxInactiveInterval(36000);
        return sessionRepository;
    }*/

    @Override
    public KeyGenerator keyGenerator() {
        KeyGenerator keyGenerator = new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                StringBuilder sb = new StringBuilder();
                sb.append(o.getClass().getName());
                sb.append(method.getName());
                for (Object obj : objects) {
                    sb.append(obj.toString()+"   :1024");
                }
                return sb.toString();
            }
        };
        //default : Arrays.deepHashCode(objects)
        return keyGenerator;
    }

}