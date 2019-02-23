package me.bhop.bcon.adapter.adapters

import me.bhop.bcon.Bcon
import me.bhop.bcon.adapter.TypeAdapter
import me.bhop.bcon.node.*
import java.lang.reflect.ParameterizedType

object DefaultTypeAdapter : TypeAdapter<Any> {
    @Suppress("UNCHECKED_CAST")
    override fun toBcon(bcon: Bcon, t: Any, parent: ParentNode, id: String, comments: MutableList<String>): Node {
        val root = OrphanNode()
        for (field in t::class.java.declaredFields) {
            field.isAccessible = true
            val fValue = field.get(t)
            val adapter = bcon.getTypeAdapter(fValue.javaClass, true)
            when {
                fValue is List<*> && adapter == null -> {
                    val listType = (field.genericType as ParameterizedType).actualTypeArguments[0].typeName
                    val array = ArrayNode(field.name, mutableListOf(), parent.asNode()) //todo move this to one of the default global type adapters. This class should only handle unknown stuff
                    when (listType) { //todo do this better, it is awful and ugly :(
                        "java.lang.String" ->
                            for (str in (fValue as List<String>))
                                array.add(StringNode("_array", mutableListOf(), array, str))
                        "java.lang.Boolean" ->
                            for (bool in (fValue as List<Boolean>))
                                array.add(BooleanNode("_array", mutableListOf(), array, bool))
                        "java.lang.Integer", "java.lang.Double", "java.lang.Long", "java.lang.Short", "java.lang.Float" ->
                            for (num in (fValue as List<Number>))
                                array.add(NumberNode("_array", mutableListOf(), array, num))
                        else -> {
                            println("work in progress, needs to add them to a category with the names 0, 1, 2, etc and use type adapters to convert them.")
                        }
                    }
                    root.add(node = array)
                }
                adapter == null -> root.add(node = (toBcon(bcon, field.get(t), root, field.name, mutableListOf()) as OrphanNode).toCategory(field.name, mutableListOf(), root)) //todo comments
                else -> root.add(node = adapter.toBcon(bcon, field.get(t), root, field.name, mutableListOf()))
            } //todo no way to get comments without annotation, but should check for setting annotation in every case!
        }

        return root
    }

    override fun fromBcon(bcon: Bcon, node: Node): Any {
        TODO()
    }

    override fun getType(): Class<Any> = Any::class.java

}