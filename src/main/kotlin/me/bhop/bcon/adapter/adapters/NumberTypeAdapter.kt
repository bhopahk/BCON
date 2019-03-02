package me.bhop.bcon.adapter.adapters

import me.bhop.bcon.Bcon
import me.bhop.bcon.adapter.TypeAdapter
import me.bhop.bcon.node.Node
import me.bhop.bcon.node.NumberNode
import me.bhop.bcon.node.ParentNode

class NumberTypeAdapter : TypeAdapter<Number> {
    override val type: Class<Number> = Number::class.java

    override fun toBcon(
        bcon: Bcon,
        t: Number,
        parent: ParentNode,
        id: String,
        comments: MutableList<String>,
        oType: Class<*>?
    ): Node = NumberNode(id, comments, parent.asNode(), t)

    override fun fromBcon(bcon: Bcon, node: Node): Number = node.getAsPrimitive()?.getAsNumber() ?: throw IllegalArgumentException("Supplied node is not of type Number.")
}