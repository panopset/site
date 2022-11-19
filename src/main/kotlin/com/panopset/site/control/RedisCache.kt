package com.panopset.site.control

import com.panopset.site.RedisClientAPI
import com.panopset.site.RedisClientJedisImpl
import org.springframework.stereotype.Component

@Component
class RedisCache {

    fun get(key: String): String {
        return redisClient.get(key)
    }

    fun put(key: String, value: String) {
        redisClient.put(key, value)
    }

    fun getError(): String {
        return redisClient.getError()
    }

    val redisClient: RedisClientAPI = RedisClientJedisImpl()
}
