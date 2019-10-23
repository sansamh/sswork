package io.sansam.sswork.common.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;

/**
 * <p>
 * RedissonConfig
 * </p>
 *
 * @author houcb
 * @since 2019-10-23 15:13
 */
@Slf4j
@Configuration
public class RedissonConfig {

    @Autowired
    RedisProperties redisProperties;

    @Bean(name = "singleRedissonClient")
    @Nullable
    public RedissonClient getSingleRedissonClient() {
        Config config = new Config();
        final String address = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();
        config.useSingleServer()
                .setAddress(address)
                .setPassword(redisProperties.getPassword());
        try {
            return Redisson.create(config);
        } catch (Exception e) {
            log.error("RedissonConfig - getSingleRedissonClient init redis url = [{}], Exception = ", address, e);
            return null;
        }
    }
}
