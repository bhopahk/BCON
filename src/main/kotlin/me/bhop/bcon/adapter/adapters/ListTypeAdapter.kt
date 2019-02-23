package me.bhop.bcon.adapter.adapters

import me.bhop.bcon.Bcon
import me.bhop.bcon.adapter.TypeAdapter
import me.bhop.bcon.node.ArrayNode
import me.bhop.bcon.node.Node
import me.bhop.bcon.node.ParentNode

class ListTypeAdapter : TypeAdapter<List<Any>> {
    override fun toBcon(bcon: Bcon, t: List<Any>, parent: ParentNode, id: String, comments: MutableList<String>): Node {
        val array = ArrayNode(id, comments, parent.asNode())

        try {
            t as List<String>
            println("casted fine")
        } catch (e: ClassCastException) {
            println("Not a string")
        }

//        val listType = ( as ParameterizedType).actualTypeArguments[0].typeName
//
//        val array = ArrayNode(field.name, mutableListOf(), parent.asNode()) //todo move this to one of the default global type adapters. This class should only handle unknown stuff
//        when (listType) { //todo do this better, it is awful and ugly :(
//            "java.lang.String" ->
//                for (str in (fValue as List<String>))
//                    array.add(StringNode("_array", mutableListOf(), array, str))
//            "java.lang.Boolean" ->
//                for (bool in (fValue as List<Boolean>))
//                    array.add(BooleanNode("_array", mutableListOf(), array, bool))
//            "java.lang.Integer", "java.lang.Double", "java.lang.Long", "java.lang.Short", "java.lang.Float" ->
//                for (num in (fValue as List<Number>))
//                    array.add(NumberNode("_array", mutableListOf(), array, num))
//            else -> {
//                println("work in progress, needs to add them to a category with the names 0, 1, 2, etc and use type adapters to convert them.")
//            }
//        }
//        root.add(node = array)
        TODO()
    }

    override fun fromBcon(bcon: Bcon, node: Node): List<Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(): Class<List<Any>> = List::class.java as Class<List<Any>>
}