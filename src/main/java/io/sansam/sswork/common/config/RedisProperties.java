package io.sansam.sswork.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * RedisProperties
 * </p>
 *
 * @author houcb
 * @since 2019-10-23 15:53
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    private String host;
    private int port;
    private String password;
    private int database;
}
