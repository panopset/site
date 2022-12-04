package com.panopset.games.denebola.control

import com.google.gson.Gson
import com.panopset.compat.Jsonop
import com.panopset.games.denebola.Toard
import com.panopset.games.denebola.Tronk
import com.panopset.site.*
import com.panopset.site.control.Config
import com.panopset.site.control.PanBase
import com.panopset.site.control.SystemPropertyMap
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class DenebolaController(private val config: Config, private val panBase: PanBase, private val systemPropertyMap: SystemPropertyMap) {

    val gameKey = "gameKey"

    @GetMapping(*["/denebola"])
    fun denebola(model: Model?, response: HttpServletResponse?): String? {
        if (model != null) {
            panInit(model)
            model.addAttribute("error", panBase.rc.getError())
        }
        return "denebola/denebola"
    }

    @GetMapping("/denebola_hsl")
    fun highScoreList(model: Model?, response: HttpServletResponse?): String? {
        if (model != null) {
            panInit(model)
        }
        return "denebola/denebola_hsl"
    }

    @GetMapping("/denebola_config")
    fun config(model: Model?, request: HttpServletRequest, response: HttpServletResponse?): String? {
        if (model != null) {
            panInit(model)

            request.localName

            systemPropertyMap.map["request localName"] = request.localName ?: "undefined"
            systemPropertyMap.map["request localAddr"] = request.localAddr ?: "undefined"
            systemPropertyMap.map["request remoteAddr"] = request.remoteAddr ?: "undefined"
            systemPropertyMap.map["request remoteUser"] = request.remoteUser ?: "undefined"
            systemPropertyMap.map["request remoteHost"] = request.remoteHost ?: "undefined"
            systemPropertyMap.map["session ID"] = request.session?.id ?: "undefined"

            for (headerName in request.headerNames) {

                systemPropertyMap.map["request header $headerName"] = request.getHeader(headerName)
            }


            model.addAttribute("svr", systemPropertyMap.map)
        }
        return "denebola/denebola_config"
    }

    private fun getRandomHexString(numchars: Int): String? {
        val r = Random()
        val sb = StringBuffer()
        while (sb.length < numchars) {
            sb.append(Integer.toHexString(r.nextInt()))
        }
        return sb.toString().substring(0, numchars)
    }

    private fun createInitialToard(sessionId: String): Toard {
        val toard = Toard()
        val tronk = Tronk()
        tronk.id = UUID.randomUUID().toString()
        tronk.owner = sessionId
        val random3hex = getRandomHexString(3)
        tronk.fill = "#${random3hex}"
        tronk.stroke = "#f0f"
        tronk.x = 120
        tronk.y = 300
        tronk.r = 40
        tronk.tx = 120
        tronk.ty = 300
        toard.tronks.add(tronk)
        return toard
    }

    private fun getToard(sessionId: String): Toard {
        val jsonString = panBase.rc.get(gameKey)
        var toard: Toard?
        if (jsonString.isBlank()) {
            toard = createInitialToard(sessionId)
            panBase.rc.put(gameKey, Jsonop().toJson(toard))
        } else {
            toard = Jsonop().fromJson(jsonString, Toard::class.java) as Toard?
            if (toard == null) {
                toard = createInitialToard(sessionId)
            }
        }
        return toard
    }

    @GetMapping("/denebola/ajaxInit")
    fun gameInit(request: HttpServletRequest, response: HttpServletResponse?): ResponseEntity<String?>? {
        return ResponseEntity.ok(Jsonop().toJson(getToard(request.session.id)))
    }

    @PostMapping("/denebola/ajaxGetTarget")
    fun getResult(@RequestBody userTronk: Tronk, request: HttpServletRequest, response: HttpServletResponse?): ResponseEntity<String?>? {
        val toard = getToard(request.session.id)
        for (tronk in toard.tronks) {
            if (tronk.id == userTronk.id) {
                val random3hex = getRandomHexString(3)
                tronk.fill = "#${random3hex}"
                saveToard(toard)
                return ResponseEntity.ok(Jsonop().toJson(tronk))
            }
        }
        val errorMsg = panBase.rc.getError()
        if (errorMsg.isNotEmpty()) {
            ResponseEntity.internalServerError().body(errorMsg)
        }
        return ResponseEntity.ok("")
    }

    private fun panInit(model: Model) {
        model.addAttribute("env", config.env)
        model.addAttribute("host", config.host)
        model.addAttribute("redis_url", REDIS_URL)
        model.addAttribute("redis_pwd", REDIS_PWD)
    }

    private fun saveToard(toard: Toard) {
        panBase.rc.put(gameKey, Jsonop().toJson(toard))
    }
}
