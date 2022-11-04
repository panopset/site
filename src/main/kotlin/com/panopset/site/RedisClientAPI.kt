package com.panopset.site

interface RedisClientAPI {
    abstract fun getError(): String
    abstract fun get(key: String): String
    abstract fun put(key: String, value: String)
}
