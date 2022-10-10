package com.panopset.site

const val RUK = "PANOPSET_SITE_REDIS_URL"
const val RPK = "PANOPSET_SITE_REDIS_PWD"
val REDIS_URL = System.getenv()[RUK]
val REDIS_PWD = System.getenv()[RPK]

fun isEnvValid(): Boolean {
    if (REDIS_URL == null || REDIS_PWD == null) {
        return false
    }
    if (REDIS_URL.isBlank()) {
        return false
    }
    if (REDIS_PWD.isBlank()) {
        return false
    }
    return true
}

fun failedEnvMessage(): String {
    return "FATAL: Environment variables $RUK and/or $RPK not defined."
}
