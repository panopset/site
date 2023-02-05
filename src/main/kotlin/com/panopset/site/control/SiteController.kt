package com.panopset.site.control

import com.panopset.compat.JavaVersionChart
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class SiteController {

    @GetMapping(*["/", "/home", "/index", "/index.htm", "/index.html"])
    fun home(model: Model?, response: HttpServletResponse?): String? {
        return "index"
    }

    @GetMapping(*["/checksum", "/checksum.htm", "/checksum.html"])
    fun checksum(model: Model?, response: HttpServletResponse?): String? {
        return "checksum"
    }

    @GetMapping(*["/lowerclass", "/lowerclass.htm", "/lowerclass.html"])
    fun lowerclass(model: Model, response: HttpServletResponse?): String? {
        model.addAttribute("chart", JavaVersionChart.getChart())
        return "lowerclass"
    }
    @GetMapping(*["/blackjack", "/blackjack.htm", "/blackjack.html"])
    fun blackjack(model: Model?, response: HttpServletResponse?): String? {
        return "blackjack"
    }

    @GetMapping(*["/build", "/build.htm", "/build.html"])
    fun build(model: Model?, response: HttpServletResponse?): String? {
        return "build"
    }

    @GetMapping(*["/news", "/news.htm", "/news.html"])
    fun news(request: HttpServletRequest?, model: Model?, response: HttpServletResponse?): String? {
        return "news"
    }

    @GetMapping(*["/about", "/about.htm", "/about.html"])
    fun about(model: Model?, response: HttpServletResponse?): String? {
        return "about"
    }

    @GetMapping(*["/design", "/design.htm", "/design.html"])
    fun design(model: Model?, response: HttpServletResponse?): String? {
        return "design"
    }

    @GetMapping(*["/lynx", "/lynx.htm", "/lynx.html"])
    fun lynx(model: Model?, response: HttpServletResponse?): String? {
        return "lynx"
    }

    @GetMapping(*["/cfg", "/cfg.htm", "/cfg.html"])
    fun cfg(model: Model?, response: HttpServletResponse?): String? {
        return "personalize"
    }
}
