package me.bhop.bcon.io

import me.bhop.bcon.node.*
import java.text.NumberFormat

enum class NodeType {
    CATEGORY {
        override fun toNode(id: String, comments: MutableList<String>, parent: Node, value: String, data: NodeType): Node = throw IllegalStateException("Not to be used to generate categories.")

    },
    ARRAY {
        override fun toNode(id: String, comments: MutableList<String>, parent: Node, value: String, data: NodeType): Node {
            // value = "test","test2","etc"
            // value = 1,2,3,4,5,6,7,8,9
            // value = true,true,false,false,true
            val arr = ArrayNode(id, comments, parent)
            for (str in value.substring(0, value.length-1).split("φ")) {
                val element = data.toNode("_array", mutableListOf(), arr, str).getAsPrimitive()
                if (element != null)
                    arr.add(element)
            }
            return arr
        }
    },
    PRIMITIVE_STRING {
        override fun toNode(id: String, comments: MutableList<String>, parent: Node, value: String, data: NodeType) = StringNode(id, comments, parent, value)
    },
    PRIMITIVE_NUMBER {
        override fun toNode(id: String, comments: MutableList<String>, parent: Node, value: String, data: NodeType): Node = NumberNode(id, comments, parent, NumberFormat.getNumberInstance().parse(value))
    },
    PRIMITIVE_BOOLEAN {
        override fun toNode(id: String, comments: MutableList<String>, parent: Node, value: String, data: NodeType): Node = BooleanNode(id, comments, parent, value.toBoolean())
    },
    UNKNOWN {
        override fun toNode(id: String, comments: MutableList<String>, parent: Node, value: String, data: NodeType) = throw IllegalStateException("UNKNOWN is not a valid node type.")
    };

    abstract fun toNode(id: String, comments: MutableList<String>, parent: Node, value: String, data: NodeType = UNKNOWN): Node
}