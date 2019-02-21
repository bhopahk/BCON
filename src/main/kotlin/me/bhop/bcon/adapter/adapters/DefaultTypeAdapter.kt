package me.bhop.bcon.adapter.adapters

import me.bhop.bcon.Bcon
import me.bhop.bcon.adapter.TypeAdapter
import me.bhop.bcon.node.Node

object DefaultTypeAdapter : TypeAdapter<Any> {
    override fun toBcon(bcon: Bcon, t: Any): Node {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fromBcon(bcon: Bcon, node: Node): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(): Class<Any> = Any::class.java
}