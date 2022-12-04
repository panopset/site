package com.panopset.site.control

import org.springframework.stereotype.Component
import java.util.*

@Component
class SystemPropertyMap(config: Config) {

        final val map: SortedMap<String, String> = Collections.synchronizedSortedMap(TreeMap())
        init {
            map["Runtime"] = System.getProperty("java.version")
            map["VM name"] = System.getProperty("java.vm.name")
            map["VM vendor"] = System.getProperty("java.vm.vendor")
            map["Runtime Version"] = System.getProperty("java.runtime.version")
            map["Runtime Name"] = System.getProperty("java.runtime.name")
            map["Runtime Vendor"] = System.getProperty("java.vendor")
            map["Runtime Vendor URL"] = System.getProperty("java.vendor.url")
            map["OS Arch"] = System.getProperty("os.arch")
            map["OS Name"] = System.getProperty("os.name")
            map["OS Version"] = System.getProperty("os.version")
            map["Host"] = config.host
        }
}
