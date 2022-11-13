package com.panopset.site

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.util.*
import javax.servlet.http.HttpServletResponse

@Controller
class SiteController(private val config: Config, private val panBase: PanBase) {

    @GetMapping(*["/", "/home", "/index", "/index.htm", "/index.html"])
    fun home(model: Model?, response: HttpServletResponse?): String? {
        return "index"
    }

}
