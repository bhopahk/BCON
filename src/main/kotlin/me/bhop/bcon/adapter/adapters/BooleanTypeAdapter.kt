package me.bhop.bcon.adapter.adapters

import me.bhop.bcon.Bcon
import me.bhop.bcon.adapter.TypeAdapter
import me.bhop.bcon.node.BooleanNode
import me.bhop.bcon.node.Node
import me.bhop.bcon.node.ParentNode

class BooleanTypeAdapter : TypeAdapter<Boolean> {
    override fun toBcon(bcon: Bcon, t: Boolean, parent: ParentNode, id: String, comments: MutableList<String>): Node = BooleanNode(id, comments, parent.asNode(), t)

    override fun fromBcon(bcon: Bcon, node: Node): Boolean = node.getAsPrimitive()?.getAsBoolean() ?: throw IllegalArgumentException("Supplied node is not of type Boolean!")

    override fun getType(): Class<Boolean> = Boolean::class.java
}