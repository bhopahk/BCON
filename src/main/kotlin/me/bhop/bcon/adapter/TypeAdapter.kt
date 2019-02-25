package me.bhop.bcon.adapter

import me.bhop.bcon.Bcon
import me.bhop.bcon.node.Node
import me.bhop.bcon.node.ParentNode

interface TypeAdapter<T> {
    val type: Class<T>

    fun toBcon(bcon: Bcon, t: T, parent: ParentNode, id: String, comments: MutableList<String>): Node

    fun fromBcon(bcon: Bcon, node: Node): T
}