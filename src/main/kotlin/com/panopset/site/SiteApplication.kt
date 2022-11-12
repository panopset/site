package com.panopset.site

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.panopset.site", "com.panopset.games.muck")
class SiteApplication

fun main(args: Array<String>) {
	runApplication<SiteApplication>(*args)
}
