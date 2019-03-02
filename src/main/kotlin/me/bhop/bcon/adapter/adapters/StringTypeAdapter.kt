package me.bhop.bcon.adapter.adapters

import me.bhop.bcon.Bcon
import me.bhop.bcon.adapter.TypeAdapter
import me.bhop.bcon.node.Node
import me.bhop.bcon.node.ParentNode
import me.bhop.bcon.node.StringNode

class StringTypeAdapter() : TypeAdapter<String> {
    override val type: Class<String> = String::class.java

    override fun toBcon(
        bcon: Bcon,
        t: String,
        parent: ParentNode,
        id: String,
        comments: MutableList<String>,
        oType: Class<*>?
    ): Node = StringNode(id, comments, parent.asNode(), t)

    override fun fromBcon(bcon: Bcon, node: Node): String = node.getAsPrimitive()?.getAsString() ?: throw IllegalArgumentException("Supplied node is not of type String.")
}