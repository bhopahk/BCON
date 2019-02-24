package me.bhop.bcon.adapter.adapters

import me.bhop.bcon.Bcon
import me.bhop.bcon.adapter.TypeAdapter
import me.bhop.bcon.annotation.Setting
import me.bhop.bcon.node.*
import java.lang.reflect.ParameterizedType

object DefaultTypeAdapter : TypeAdapter<Any> {
    @Suppress("UNCHECKED_CAST")
    override fun toBcon(bcon: Bcon, t: Any, parent: ParentNode, id: String, comments: MutableList<String>): Node {
        val root = OrphanNode()
        fields@for (field in t::class.java.declaredFields) {
            field.isAccessible = true

            val setting = field.getAnnotation(Setting::class.java)
            val fValue = field.get(t)
            val adapter = bcon.getTypeAdapter(fValue.javaClass, true)
            val name = setting?.key ?: field.name
            val childComments: MutableList<String> = setting?.comments?.toMutableList() ?: mutableListOf()
            when {
                fValue is List<*> && adapter == null -> { //todo this needs to be moved to a different type adapter, however it fails horribly without the field access so that is a problem.
                    val listType = (field.genericType as ParameterizedType).actualTypeArguments[0].typeName
                    val array = ArrayNode(name, childComments, parent.asNode())
                    when (listType) {
                        "java.lang.String" ->
                            for (str in (fValue as List<String>))
                                array.add(StringNode("_array", parent = array, value = str))
                        "java.lang.Boolean" ->
                            for (bool in (fValue as List<Boolean>))
                                array.add(BooleanNode("_array", parent = array, value = bool))
                        "java.lang.Integer", "java.lang.Double", "java.lang.Long", "java.lang.Short", "java.lang.Float" ->
                            for (num in (fValue as List<Number>))
                                array.add(NumberNode("_array", parent = array, value = num))
                        else -> {
                            val category = CategoryNode(name, childComments, parent.asNode())
                            for (i in 0 until fValue.size) {
                                val element = fValue[i]
                                if (element != null)
                                    category.add(node = toBcon(bcon, element, category, "$i", mutableListOf()))
                            }
                            root.add(node = category)
                            continue@fields
                        }
                    }
                    root.add(node = array)
                }
                adapter == null -> root.add(node = (toBcon(bcon, field.get(t), root, name, childComments) as OrphanNode).toCategory(id, childComments, root))
                else -> root.add(node = adapter.toBcon(bcon, field.get(t), root, name, childComments))
            }
        }

        return root
    }

    override fun fromBcon(bcon: Bcon, node: Node): Any {
        TODO()
    }

    override fun getType(): Class<Any> = Any::class.java

}