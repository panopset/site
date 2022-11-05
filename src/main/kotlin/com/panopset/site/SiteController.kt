package com.panopset.site

import com.google.gson.Gson
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*
import javax.servlet.http.HttpServletResponse

@Controller
class SiteController(private val config: Config, private val panBase: PanBase) {

    @GetMapping(*["/", "/home", "/index", "/index.htm", "/index.html"])
    fun home(model: Model?, response: HttpServletResponse?): String? {
        if (model != null) {
            panInit(model)
            model.addAttribute("error", panBase.rc.getError())
        }
        return "index"
    }

    @GetMapping("/hsl")
    fun highScoreList(model: Model?, response: HttpServletResponse?): String? {
        if (model != null) {
            panInit(model)
        }
        return "hsl"
    }

    @GetMapping("/config")
    fun config(model: Model?, response: HttpServletResponse?): String? {
        if (model != null) {
            panInit(model)
            model.addAttribute("svr", SystemPropertyMap.map)
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
        val newFill = "#$random3hex"
        tronk.fill = newFill
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
        model.addAttribute("redis_url", REDIS_URL)
        model.addAttribute("redis_pwd", REDIS_PWD)
    }
}
