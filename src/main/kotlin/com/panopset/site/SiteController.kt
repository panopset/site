package com.panopset.site

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletResponse

@Controller
class SiteController(private val config: Config) {
    @GetMapping(*["/", "/home", "/index", "/index.htm", "/index.html"])
    fun home(model: Model?, response: HttpServletResponse?): String? {
        if (model != null) {
            init(model)
        }
        return "index"
    }

    @GetMapping("/fronk")
    fun fronk(model: Model?, response: HttpServletResponse?): String? {
        if (model != null) {
            init(model)
        }
        return "fronk"
    }

    private fun init(model: Model) {
        model.addAttribute("foo", "bar")
        model.addAttribute("env", config.env)
        model.addAttribute("redis_url", System.getenv().get("PANOPSET_SITE_REDIS_URL"))
        model.addAttribute("redis_pwd", System.getenv().get("PANOPSET_SITE_REDIS_PWD"))
    }
}
