package me.bhop.bcon.adapter.adapters

import me.bhop.bcon.Bcon
import me.bhop.bcon.adapter.TypeAdapter
import me.bhop.bcon.annotation.Setting
import me.bhop.bcon.node.Node
import me.bhop.bcon.node.OrphanNode
import me.bhop.bcon.node.ParentNode
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

object DefaultTypeAdapter : TypeAdapter<Any> {
    override val type: Class<Any> = Any::class.java

    override fun toBcon(bcon: Bcon, t: Any, parent: ParentNode, id: String, comments: MutableList<String>, oType: Class<*>?): Node {
        val root = OrphanNode()
        for (field in t::class.java.declaredFields) {
            if (Modifier.isStatic(field.modifiers)) continue
            field.isAccessible = true

            println("-> ${field.name}/${field.type.name} -> $t")

            val setting = field.getAnnotation(Setting::class.java)
            val name = setting?.key ?: field.name
            val childComments: MutableList<String> = setting?.comments?.toMutableList() ?: mutableListOf()
            val value = field.get(t) ?: continue //todo dont skip null in the future
            val adapter = bcon.getTypeAdapter(value.javaClass, orSuper = true, orGlobal = true) //todo configurable within bcon class

            println("\tType ${value::class.java.name} has adapter $adapter")

            val generic = (field?.genericType as? ParameterizedType)?.actualTypeArguments?.get(0)?.typeName

            if (adapter == null) {
                root.add(node = (DefaultTypeAdapter.toBcon(bcon, value, root, name, childComments, if (generic != null) generic::class.java else null) as OrphanNode).toCategory(name, childComments, root))
            } else
                root.add(node = adapter.toBcon(bcon, value, root, name, childComments, if (generic != null) Class.forName(generic) else null))
        }
        return root
    }

    override fun fromBcon(bcon: Bcon, node: Node): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}