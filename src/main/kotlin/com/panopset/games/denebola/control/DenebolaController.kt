package com.panopset.games.denebola.control

import com.google.gson.Gson
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
import javax.servlet.http.HttpServletResponse

@Controller
class DenebolaController(private val config: Config, private val panBase: PanBase, private val systemPropertyMap: SystemPropertyMap) {
    @GetMapping(*["/denebola.html", "/denebola"])
    fun denebola(model: Model?, response: HttpServletResponse?): String? {
        if (model != null) {
            panInit(model)
            model.addAttribute("error", panBase.rc.getError())
        }
        return "denebola/denebola"
    }

    @GetMapping("/denebola_hsl.html")
    fun highScoreList(model: Model?, response: HttpServletResponse?): String? {
        if (model != null) {
            panInit(model)
        }
        return "denebola/denebola_hsl"
    }

    @GetMapping("/denebola_config.html")
    fun config(model: Model?, response: HttpServletResponse?): String? {
        if (model != null) {
            panInit(model)
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

    @PostMapping("/denebola/ajaxGetTarget")
    fun getResult(@RequestBody tronk: Tronk, response: HttpServletResponse?): ResponseEntity<String?>? {
        if (tronk.id.isNotBlank()) {
            val jsonString = panBase.rc.get(tronk.id)
            if (jsonString.isNotBlank()) {
                val newTronk = Gson().fromJson(jsonString, Tronk::class.java)
                // TODO: make whatever adjustments to existing
                return ResponseEntity.ok(Gson().toJson(newTronk))
            }
        }
        tronk.id = UUID.randomUUID().toString()
        val random3hex = getRandomHexString(3)
        tronk.fill = "#${random3hex}"
        val tronkJson = Gson().toJson(tronk)
        panBase.rc.put(tronk.id, tronkJson)
        val errorMsg = panBase.rc.getError()
        if (errorMsg.isNotEmpty()) {
            ResponseEntity.internalServerError().body(errorMsg)
        }
        return ResponseEntity.ok(tronkJson)
    }

    private fun panInit(model: Model) {
        model.addAttribute("env", config.env)
        model.addAttribute("host", config.host)
        model.addAttribute("redis_url", REDIS_URL)
        model.addAttribute("redis_pwd", REDIS_PWD)
    }
}