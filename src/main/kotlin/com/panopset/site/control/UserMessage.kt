package com.panopset.site.control

import org.springframework.stereotype.Component

/**
 * If there is a fatal error, display the first one.
 */
@Component
class UserMessage {
   var message = ""

    fun isAlive(): Boolean {
        return "" == message
    }

    fun reportError(errorMessage: String): String {
        if (isAlive()) {
            message = errorMessage
        }
        return message
    }
}
