package com.panopset.site.control

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.panopset.site.control", "com.panopset.games.denebola.control")
class SiteApplication

fun main(args: Array<String>) {
	runApplication<SiteApplication>(*args)
}
