package com.panopset.site

import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

/**
 * TODO asynch https://spring.io/guides/gs/async-method/
 */
class RedisClientJedisImpl : RedisClientAPI {

    var jedisPool: JedisPool
    val poolConfig = JedisPoolConfig()
    var jedisError = ""

    init {
        poolConfig.maxIdle = 20
        poolConfig.minIdle = 1
        val i = REDIS_URL?.lastIndexOf(":") ?: -1
        val url0 = REDIS_URL
        var host = url0
        var port = 6379
        if (i > 7) {
            host = url0?.substring(0, i) ?: ""
            val port0 = url0?.substring(i + 1) ?: ""
            port = port0.toInt()
        }
        if (host.isNullOrBlank()) {
            jedisError = failedEnvMessage()
        }
        jedisPool = JedisPool(poolConfig, host, port, 3000, REDIS_PWD)
    }

    override fun getError(): String {
        return jedisError
    }

    override fun get(key: String): String {
        try {
            val jedis = jedisPool.resource
            return jedis.get(key)
        } catch (t: Throwable) {
            jedisError = t.message ?: ""
        }
        return ""
    }

    override fun put(key: String, value: String) {
        try {
            val jedis = jedisPool.resource
            jedis.setex(key, 604800, value)
        } catch (t: Throwable) {
            jedisError = t.message ?: ""
        }
    }
}