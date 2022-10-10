package com.panopset.site

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SiteApplication

fun main(args: Array<String>) {
	if (!isEnvValid()) {
		println(failedEnvMessage())
		return
	}
	runApplication<SiteApplication>(*args)
}
