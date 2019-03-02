package me.bhop.bcon.adapter.adapters

import me.bhop.bcon.Bcon
import me.bhop.bcon.adapter.TypeAdapter
import me.bhop.bcon.node.*

@Suppress("UNCHECKED_CAST")
class CollectionTypeAdapter : TypeAdapter<Collection<Any>> {
    override val type: Class<Collection<Any>> = Class.forName("java.util.Collection") as Class<Collection<Any>>

    override fun toBcon(
        bcon: Bcon,
        t: Collection<Any>,
        parent: ParentNode,
        id: String,
        comments: MutableList<String>,
        oType: Class<*>?
    ): Node {
        if (oType == null)
            throw RuntimeException("Failed to detect type of collection!")
        return when (oType) {
            String::class.java -> {
                val array = ArrayNode(id, comments, parent.asNode())
                for (str in t as Collection<String>)
                    array.add(StringNode("_array", parent = array, value = str))
                parent.add(node = array)
                array
            }
            Boolean::class.java -> {
                val array = ArrayNode(id, comments, parent.asNode())
                for (bool in t as Collection<Boolean>)
                    array.add(BooleanNode("_array", parent = array, value = bool))
                parent.add(node = array)
                array
            }
            Int::class.java, Double::class.java, Long::class.java, Short::class.java, Float::class.java -> {
                val array = ArrayNode(id, comments, parent.asNode())
                for (num in t as Collection<Number>)
                    array.add(NumberNode("_array", parent = array, value = num))
                parent.add(node = array)
                array
            }
            else -> {
                val category = CategoryNode(id, comments, parent.asNode())
                val listOfColl = t.toList() //todo remove this list call
                for (i in 0 until listOfColl.size) {
                    val v = listOfColl[i]
                    category.add(node = bcon.getDefaultTypeAdapter().toBcon(bcon, v, category, "$i", mutableListOf())) //todo configurable options here and elsewhere

                }
                parent.add(node = category)
                category
            }
        }
    }

    override fun fromBcon(bcon: Bcon, node: Node): Collection<Any> {
        TODO("not implementedddddddddddddd") //To change body of created functions use File | Settings | File Templates.
    }

    inline fun <reified T: Any> Collection<T>.genericType(): Class<T> {
        return T::class.java
    }
}