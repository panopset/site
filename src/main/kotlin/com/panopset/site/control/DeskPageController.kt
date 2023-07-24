package com.panopset.site.control

import com.panopset.compat.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.util.*

@Controller
class DeskPageController(private val config: Config) {

    val HOMEDIRPFX = "homedirpfx"
    val USERPATHSEP = "userpathsep"
    val PLATFORMS = arrayOf("linux", "mac", "win")
    val INSTALLERS = arrayOf(
        arrayOf("linux", String.format("panopset_%s-1_amd64.deb", "1.2.4")),
        arrayOf("mac", String.format("panopset-%s.dmg", "1.2.4")),
        arrayOf("win", String.format("panopset-%s.msi", "1.2.4"))
    )

    @GetMapping(*["/desk", "/desk.htm", "/desk.html"])
    fun desk(request: HttpServletRequest?, model: Model?, response: HttpServletResponse?): String? {
        setupHomeDirPfx(request, model)
        return "desk"
    }

    @GetMapping(*["/download", "/download.htm", "/download.html"])
    fun download(request: HttpServletRequest?, model: Model, response: HttpServletResponse?): String? {
        model.addAttribute("version", AppVersion.version)
        model.addAttribute("pfx", PLATFORMS)
        model.addAttribute("jar_map", getPanopsetJarValidationMap())
        model.addAttribute("installer_map", getInstallerValidationMap())
        setupHomeDirPfx(request, model)
        return "download"
    }

    private var panopsetJarMap: Map<String, List<NameValuePair>> = Collections.synchronizedSortedMap(TreeMap())
    private var installerMap: MutableMap<String, List<NameValuePair>> = Collections.synchronizedSortedMap(TreeMap())

    private fun getPanopsetJarValidationMap(): Map<String, List<NameValuePair>> {
        if (panopsetJarMap.isEmpty()) {
            for (platform in PLATFORMS) {
                var cil: List<NameValuePair> = ArrayList()
                val pci =
                    UrlHelper.getTextFromURL(String.format("%s/dt/gen/json/pci_%s.json", config.host, platform))
                if (Stringop.isPopulated(pci)) {
                    @Suppress("UNCHECKED_CAST")
                    cil = Jsonop().fromJson(pci, cil.javaClass) as List<NameValuePair>
                    safeAdd(arrayOf(platform, "panopset.jar"),
                        panopsetJarMap as SortedMap<String, List<NameValuePair>>, cil)
                }
            }
        }
        return panopsetJarMap
    }

    private fun getInstallerValidationMap(): Map<String, List<NameValuePair>> {
        if (installerMap.isEmpty()) {
            for (installers in INSTALLERS) {
                var cil: List<NameValuePair> = ArrayList()
                val url = String.format("%s/dt/gen/json/pci_%s.json", config.host, installers[1])
                Logop.info(String.format("%s : %s, %s", installers[0], installers[1], url))
                val pci = UrlHelper.getTextFromURL(url)
                if (Stringop.isPopulated(pci)) {
                    @Suppress("UNCHECKED_CAST")
                    cil = Jsonop().fromJson(pci, cil.javaClass) as List<NameValuePair>
                    if (cil.isEmpty()) {
                        Logop.dspmsg(String.format("URL found, but still unable to load installers from %s.", url))
                    }
                    safeAdd(installers, installerMap, cil)
                } else {
                    Logop.dspmsg(String.format("Unable to load installers from %s.", url))
                }
            }
        }
        return installerMap
    }

    private fun safeAdd(
        definition: Array<String>,
        map: MutableMap<String, List<NameValuePair>>,
        value: List<NameValuePair>
    ) {
        if (value.size > 3) {
            map[definition[0]] = value
        } else {
            map[definition[0]] = createDummyList(definition[1])
        }
    }

    private fun createDummyList(missing: String): List<NameValuePair> {
        val dummyList: MutableList<NameValuePair> = ArrayList()
        dummyList.add(NameValuePair("version", "1.0.1"))
        dummyList.add(NameValuePair("bytes", "0"))
        dummyList.add(NameValuePair(missing, "not found"))
        dummyList.add(NameValuePair("ifn", missing))
        return dummyList
    }

    private fun setupHomeDirPfx(request: HttpServletRequest?, model: Model?) {
        if (model == null) {
            return
        }
        if (request == null) {
            model.addAttribute(HOMEDIRPFX, "~/")
            model.addAttribute(USERPATHSEP, "/")
        }
        val ua = request?.getHeader("User-Agent") ?: ""
        if (ua.lowercase(Locale.getDefault()).contains("win")) {
            model.addAttribute(HOMEDIRPFX, "%USERPROFILE%\\")
            model.addAttribute(USERPATHSEP, "\\")
        } else {
            model.addAttribute(HOMEDIRPFX, "~/")
            model.addAttribute(USERPATHSEP, "/")
        }
    }
}
