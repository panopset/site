package com.panopset.site

import com.google.gson.Gson
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import java.util.*
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap

@Controller
class SiteController(private val config: Config) {

    companion object {
        var jedisPool: JedisPool
        val poolConfig = JedisPoolConfig()

        init {
            poolConfig.maxIdle = 20
            poolConfig.minIdle = 1
            if (REDIS_URL == null) {
                throw RuntimeException(failedEnvMessage())
            }
            if (REDIS_PWD == null) {
                throw RuntimeException(failedEnvMessage())
            }
            val i = REDIS_URL.lastIndexOf(":")
            val url0 = REDIS_URL
            var host = url0
            var port = 6379
            if (i > 7) {
                host = url0.substring(0, i)
                val port0 = url0.substring(i + 1)
                port = port0.toInt()
            }
            if (host.isBlank()) {
                throw RuntimeException(failedEnvMessage())
            }
            jedisPool = JedisPool(poolConfig, host, port, 3000, REDIS_PWD)
        }
    }

    @GetMapping(*["/", "/home", "/index", "/index.htm", "/index.html"])
    fun home(model: Model?, response: HttpServletResponse?): String? {
        if (model != null) {
            init(model)
        }
        return "index"
    }

    @GetMapping("/hsl")
    fun highScoreList(model: Model?, response: HttpServletResponse?): String? {
        if (model != null) {
            init(model)
        }
        return "hsl"
    }

    @GetMapping("/config")
    fun config(model: Model?, response: HttpServletResponse?): String? {
        if (model != null) {
            init(model)
            val map = HashMap<String, String>()
            map["Runtime"] = System.getProperty("java.version")
            map["VM name"] = System.getProperty("java.vm.name")
            map["VM vendor"] = System.getProperty("java.vm.vendor")
            map["Runtime Version"] = System.getProperty("java.runtime.version")
            map["Runtime Name"] = System.getProperty("java.runtime.name")
            map["Runtime Vendor"] = System.getProperty("java.vendor")
            map["Runtime Vendor URL"] = System.getProperty("java.vendor.url")
            map["OS Arch"] = System.getProperty("os.arch")
            map["OS Name"] = System.getProperty("os.name")
            map["OS Version"] = System.getProperty("os.version")
            model.addAttribute("svr", map)
        }
        return "config"
    }
    private fun getRandomHexString(numchars: Int): String? {
        val r = Random()
        val sb = StringBuffer()
        while (sb.length < numchars) {
            sb.append(Integer.toHexString(r.nextInt()))
        }
        return sb.toString().substring(0, numchars)
    }

    @PostMapping("/ajaxGetTarget")
    fun getResult(@RequestBody tronk: Tronk, response: HttpServletResponse?): ResponseEntity<String?>? {
        val jedis = jedisPool.resource
        if (tronk.id.isNotBlank()) {
            val jsonString = jedis.get(tronk.id)
            if (jsonString.isNotBlank()) {
                val newTronk = Gson().fromJson(jsonString, Tronk::class.java)
                // TODO: make whatever adjustments to existing
                return ResponseEntity.ok(Gson().toJson(newTronk))
            }
        }
        tronk.id = UUID.randomUUID().toString()
        val random3hex = getRandomHexString(3)
        val newFill = "#$random3hex"
        tronk.fill = newFill
        val tronkJson = Gson().toJson(tronk)
        jedis.setex(tronk.id, 604800, tronkJson)
        return ResponseEntity.ok(tronkJson)
    }

    private fun init(model: Model) {
        model.addAttribute("foo", "bar")
        model.addAttribute("env", config.env)
        model.addAttribute("redis_url", REDIS_URL)
        model.addAttribute("redis_pwd", REDIS_PWD)
    }
}
