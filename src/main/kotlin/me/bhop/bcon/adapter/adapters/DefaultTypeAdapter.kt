package me.bhop.bcon.adapter.adapters

import me.bhop.bcon.Bcon
import me.bhop.bcon.adapter.TypeAdapter
import me.bhop.bcon.node.Node
import me.bhop.bcon.node.OrphanNode
import me.bhop.bcon.node.ParentNode

object DefaultTypeAdapter : TypeAdapter<Any> {
    override fun toBcon(bcon: Bcon, t: Any, parent: ParentNode, id: String, comments: MutableList<String>): Node {
        val root = OrphanNode()
        for (field in t::class.java.declaredFields) {
            field.isAccessible = true
            val fValue = field.get(t)
            val adapter = bcon.getTypeAdapter(fValue.javaClass, true)
            if (adapter == null)
                root.add(node = (toBcon(bcon, field.get(t), root, field.name, mutableListOf()) as OrphanNode).toCategory(field.name, mutableListOf(), root)) //todo comments
            else
                root.add(node = adapter.toBcon(bcon, field.get(t), root, field.name, mutableListOf())) //todo no way to get comments without annotation, but should check for setting annotation in every case!
        }

        return root
    }

    override fun fromBcon(bcon: Bcon, node: Node): Any {
        TODO()
    }

    override fun getType(): Class<Any> = Any::class.java

}