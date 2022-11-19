package com.panopset.web

class FwInput {
    var lineBreakStr: Boolean? = null
        get() {
            if (field == null) {
                field = java.lang.Boolean.FALSE
            }
            return field
        }
    var listBreakStr: Boolean? = null
        get() {
            if (field == null) {
                field = java.lang.Boolean.FALSE
            }
            return field
        }
    var listStr: String? = null
        get() {
            if (field == null) {
                field = ""
            }
            return field
        }
    var template: String? = null
        get() {
            if (field == null) {
                field = ""
            }
            return field
        }
    var tokens: String? = null
        get() {
            if (field == null) {
                field = ""
            }
            return field
        }
    var splitz: String? = null
        get() {
            if (field == null) {
                field = ""
            }
            return field
        }
}