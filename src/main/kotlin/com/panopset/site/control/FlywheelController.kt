package com.panopset.site.control

import com.panopset.compat.Stringop
import com.panopset.flywheel.FlywheelListDriver
import com.panopset.flywheel.LineFeedRules
import com.panopset.flywheel.ReflectionInvoker
import com.panopset.flywheel.samples.FlywheelSamples
import com.panopset.web.FwInput
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger
import javax.servlet.http.HttpServletResponse

@Controller
class FlywheelController {
    @GetMapping(*["/flywheel", "/flywheel.htm", "/flywheel.html"])
    fun flywheel(model: Model, response: HttpServletResponse?): String? {
        val fwInput = FwInput()
        model.addAttribute("fwInput", fwInput)
        model.addAttribute("result", "")
        model.addAttribute("fwss", FlywheelSamples.all())
        model.addAttribute("fwfs", ReflectionInvoker.getAll())
        return "flywheel"
    }

    @PostMapping("/af")
    fun getResult(@RequestBody fwInput: FwInput, response: HttpServletResponse?): ResponseEntity<String?>? {
        var result: String
        val listParam = Stringop.stringToList(fwInput.listStr)
        try {
            result = FlywheelListDriver.Builder(listParam, fwInput.template)
                .withLineFeedRules(LineFeedRules(fwInput.lineBreakStr, fwInput.listBreakStr))
                .withTokens(fwInput.tokens).withSplitz(fwInput.splitz).build().output
        } catch (e: IOException) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "IOException in /af POST", e)
            result = e.message ?: "Unknown error in af POST occurred, see server logs."
        }
        return ResponseEntity.ok(result)
    }
}
