package com.panopset.site.control

import com.panopset.compat.*
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class DeskPageController {

    val HOMEDIRPFX = "homedirpfx"
    val USERPATHSEP = "userpathsep"
    val PLATFORMS = arrayOf("linux", "mac", "win")
    val INSTALLERS = arrayOf(
        arrayOf("linux", String.format("panopset_%s-1_amd64.deb", "1.1")),
        arrayOf("mac", String.format("panopset-%s.dmg", "1.1")),
        arrayOf("win", String.format("panopset-%s.msi", "1.1"))
    )

    @GetMapping(*["/desk", "/desk.htm", "/desk.html"])
    fun desk(request: HttpServletRequest?, model: Model?, response: HttpServletResponse?): String? {
        setupHomeDirPfx(request, model)
        return "desk"
    }

    @GetMapping(*["/download", "/download.htm", "/download.html"])
    fun download(request: HttpServletRequest?, model: Model, response: HttpServletResponse?): String? {
        model.addAttribute("version", AppVersion.getVersion())
        model.addAttribute("pfx", PLATFORMS)
        model.addAttribute("jar_map", getPanopsetJarValidationMap())
        model.addAttribute("installer_map", getInstallerValidationMap())
        setupHomeDirPfx(request, model)
        return "download"
    }

    private var panopsetJarMap: Map<String?, List<NameValuePair>>?? = null
    private var installerMap: MutableMap<String, List<NameValuePair>>? = null

    private fun getPanopsetJarValidationMap(): Map<String?, List<NameValuePair?>?>? {
        if (panopsetJarMap == null) {
            panopsetJarMap = Collections.synchronizedSortedMap(TreeMap<String, List<NameValuePair>>())
            for (platform in PLATFORMS) {
                var cil: List<NameValuePair?> = ArrayList()
                val pci =
                    UrlHelper.getTextFromURL(String.format("http://127.0.0.1/gen/json/pci_%s.json", platform))
                if (Stringop.isPopulated(pci)) {
                    cil = Jsonop().fromJson(pci, cil.javaClass) as List<NameValuePair?>
                    safeAdd(arrayOf(platform, "panopset.jar"),
                        panopsetJarMap as SortedMap<String, List<NameValuePair>>, cil)
                }
            }
        }
        return panopsetJarMap
    }
    private fun getInstallerValidationMap(): Map<String, List<NameValuePair>>? {
        if (installerMap == null) {
            installerMap = Collections.synchronizedSortedMap(TreeMap())
            for (installers in INSTALLERS) {
                var cil: List<NameValuePair> = ArrayList()
                val url = String.format("http://127.0.0.1/gen/json/pci_%s.json", installers[1])
                Logop.info(String.format("%s : %s, %s", installers[0], installers[1], url))
                val pci = UrlHelper.getTextFromURL(url)
                if (Stringop.isPopulated(pci)) {
                    cil = Jsonop().fromJson(pci, cil.javaClass) as List<NameValuePair>
                    if (cil == null || cil.isEmpty()) {
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
        map: MutableMap<String, List<NameValuePair>>?,
        value: List<NameValuePair?>
    ) {
        if (value != null && value.size > 3) {
            map!![definition[0]] = value as List<NameValuePair>
        } else {
            map!![definition[0]] = createDummyList(definition[1])
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
