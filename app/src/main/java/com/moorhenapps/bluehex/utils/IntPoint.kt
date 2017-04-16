package com.moorhenapps.bluehex.utils

data class IntPoint(var x: Int, var y: Int) {

    fun set(x: Int, y: Int): IntPoint {
        this.x = x
        this.y = y
        return this
    }

    override fun toString(): String {
        return "$x,$y"
    }
}
