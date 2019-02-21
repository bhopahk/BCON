package me.bhop.bcon.adapter

import me.bhop.bcon.Bcon
import me.bhop.bcon.node.Node

interface TypeAdapter<T> {
    fun toBcon(bcon: Bcon, t: T): Node

    fun fromBcon(bcon: Bcon, node: Node): T

    fun getType(): Class<T>
}