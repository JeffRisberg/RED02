package com.incra.service;

import com.incra.config.RedisConfig;
import redis.clients.jedis.Jedis;

/**
 * @author Jeff Risberg
 * @since 12/05/15
 */
public class RedisClient extends Jedis {

    protected RedisConfig redisConfig;

    public RedisClient(RedisConfig redisConfig) {
        super(redisConfig.getServer(), redisConfig.getPort());
        this.redisConfig = redisConfig;

        if (redisConfig.getPassword() != null) {
            this.auth(redisConfig.getPassword());
        }
    }
}
