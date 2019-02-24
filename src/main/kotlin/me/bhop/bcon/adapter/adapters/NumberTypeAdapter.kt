package me.bhop.bcon.adapter.adapters

import me.bhop.bcon.Bcon
import me.bhop.bcon.adapter.TypeAdapter
import me.bhop.bcon.node.Node
import me.bhop.bcon.node.ParentNode

class NumberTypeAdapter : TypeAdapter<Number> {
    override fun toBcon(bcon: Bcon, t: Number, parent: ParentNode, id: String, comments: MutableList<String>): Node {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fromBcon(bcon: Bcon, node: Node): Number {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(): Class<Number> = Number::class.java
}

class IntegerTypeAdapter : TypeAdapter<Int> by NumberTypeAdapter()