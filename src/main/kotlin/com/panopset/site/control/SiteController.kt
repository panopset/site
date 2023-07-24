package com.panopset.site.control

import com.panopset.compat.JavaVersionChart
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.*
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.net.URISyntaxException
import java.util.*


@Controller
class SiteController {
    private val server = "localhost"
    private val port = 8089

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

    @GetMapping("/images/**")
    @Throws(URISyntaxException::class)
    fun mirrorImages(
        method: HttpMethod?,
        request: HttpServletRequest,
        response: HttpServletResponse?
    ): ResponseEntity<*>? {
        return mirrorPath(request, response, "images")
    }

    @GetMapping("/downloads/**")
    @Throws(URISyntaxException::class)
    fun mirrorDownloads(
        method: HttpMethod?,
        request: HttpServletRequest,
        response: HttpServletResponse?
    ): ResponseEntity<*>? {
        return mirrorPath(request, response, "downloads")
    }

    @GetMapping("/installers/**")
    @Throws(URISyntaxException::class)
    fun mirrorInstallers(
        method: HttpMethod?,
        request: HttpServletRequest,
        response: HttpServletResponse?
    ): ResponseEntity<*>? {
        return mirrorPath(request, response, "installers")
    }

//    @GetMapping("/css/**")
//    @Throws(URISyntaxException::class)
//    fun mirrorCss(
//        method: HttpMethod?,
//        request: HttpServletRequest,
//        response: HttpServletResponse?
//    ): ResponseEntity<*>? {
//        return mirrorPath(request, response, "css")
//    }

    @GetMapping("/js/**")
    @Throws(URISyntaxException::class)
    fun mirrorJs(method: HttpMethod?, request: HttpServletRequest, response: HttpServletResponse?): ResponseEntity<*>? {
        return mirrorPath(request, response, "js")
    }

    @GetMapping("/gen/**")
    @Throws(URISyntaxException::class)
    fun mirrorGen(
        method: HttpMethod?,
        request: HttpServletRequest,
        response: HttpServletResponse?
    ): ResponseEntity<*>? {
        return mirrorPath(request, response, "gen")
    }

    @GetMapping("/s/**")
    @Throws(URISyntaxException::class)
    fun mirrorS(method: HttpMethod?, request: HttpServletRequest, response: HttpServletResponse?): ResponseEntity<*>? {
        return mirrorPath(request, response, "s")
    }

    @GetMapping("/dt/**")
    @Throws(URISyntaxException::class)
    fun mirrorDt(method: HttpMethod?, request: HttpServletRequest, response: HttpServletResponse?): ResponseEntity<*>? {
        return mirrorPath(request, response, "dt")
    }

    @GetMapping("/css/**")
    @Throws(URISyntaxException::class)
    fun mirrorStatic(method: HttpMethod?, request: HttpServletRequest): ResponseEntity<*>? {
        val uri = URI("http", null, server, port, request.requestURI, request.queryString, null)
        val restTemplate = RestTemplate()
        return restTemplate.exchange(
            uri, HttpMethod.GET, HttpEntity(""),
            String::class.java
        )
    }

//    @GetMapping(*["/images/**","/downloads/**","/installers/**","/css/**","/js/**","/gen/**","/s/**","/dt/**"])
//    @Throws(URISyntaxException::class)
//    fun mirrorStatic(@RequestBody body: String, method: HttpMethod?, request: HttpServletRequest): String? {
//        val uri = URI("http", null, server, port, request.requestURI, request.queryString, null)
//        val restTemplate = RestTemplate()
//        val responseEntity: ResponseEntity<String> = restTemplate.exchange(
//            uri, HttpMethod.GET, HttpEntity<String>(body),
//            String::class.java
//        )
//        return responseEntity.body
//    }
    @Throws(URISyntaxException::class)
    fun mirrorPath(request: HttpServletRequest, response: HttpServletResponse?, rawPath: String?): ResponseEntity<*>? {
        var path = rawPath ?: ""
        if (path.indexOf("/") != 0) {
            path = "/${path}"
        }
        val requestUrl = request.requestURI
        var uri = URI("http", null, server, port, "", null, null)
        uri = UriComponentsBuilder.fromUri(uri)
            .path(requestUrl)
            .query(request.queryString)
            .build(true).toUri()
        val headers = HttpHeaders()
        val headerNames = request.headerNames
        while (headerNames.hasMoreElements()) {
            val headerName = headerNames.nextElement()
            headers[headerName] = request.getHeader(headerName)
        }
        val httpEntity = HttpEntity<String>(headers)
        val restTemplate = RestTemplate()
        return try {
            restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String::class.java)
        } catch (e: HttpStatusCodeException) {
            ResponseEntity.status(e.rawStatusCode)
                .headers(e.responseHeaders)
                .body(e.responseBodyAsString)
        }
    }
}
