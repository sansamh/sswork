package io.sansam.sswork.common.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

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
            log.error("RedissonConfig - getSingleRedissonClient init failed! redis url = [{}]", address, e);
            return null;
        }
    }

//    @Bean(name = "clusterRedissonClient")
    @Nullable
    public RedissonClient getClusterRedissonClient() {
        Config config = new Config();
        String nodes = redisProperties.getNodes();
        String[] split = StringUtils.split(nodes, ",");
        if (Objects.isNull(split)) {
            log.error("RedissonConfig - getClusterRedissonClient init failed! nodes = [{}]", nodes);
            return null;
        }
        String[] address = new String[split.length];
        String prefix = "redis://";
        String s;
        for (int i = 0; i < split.length; i++) {
            s = split[i];
            if (!s.startsWith(prefix)) {
                s = prefix + s;
            }
            address[i] = s;
        }

        config.useClusterServers()
                // 集群状态扫描时间
                .setScanInterval(2000)
                .addNodeAddress(address)
                .setPassword(redisProperties.getPassword());
        try {
            return Redisson.create(config);
        } catch (Exception e) {
            log.error("RedissonConfig - getClusterRedissonClient init failed! redis url = [{}]", address, e);
            return null;
        }
    }
}
