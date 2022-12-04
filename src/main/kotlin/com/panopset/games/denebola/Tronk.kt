package com.panopset.games.denebola

data class Tronk (
    var fill: String = "",
    var id: String = "",
    var owner: String = "",
    var r: Int = 0,
    var stroke: String = "",
    var tx: Int = 0,
    var ty: Int = 0,
    var x: Int = 0,
    var y: Int = 0,
)

data class Toard (
    var tronks: MutableList<Tronk> = ArrayList()
)
