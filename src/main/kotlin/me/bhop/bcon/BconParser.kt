package me.bhop.bcon

import me.bhop.bcon.lexer.BconLexer
import me.bhop.bcon.lexer.Token
import me.bhop.bcon.lexer.Tokens
import me.bhop.bcon.node.*
import java.lang.IllegalStateException
import java.nio.file.Files
import java.nio.file.Paths
import java.text.NumberFormat

class BconParser() {

    fun fromBcon(): OrphanNode {
        val text = Files.newBufferedReader(Paths.get("./testbcon.conf")).readText()
        println(text)
        val lexer = BconLexer(text)
        lexer.lex()

        // Current Parent
        var parent: ParentNode = OrphanNode()

        // Current Element being constructed
        var started: Boolean = false // Used for potentially long elements (arrays)
        var arrayType: Tokens? = null
        var type: NodeType = NodeType.UNKNOWN
        var name: String? = null
        var value: String? = null
        var seenColon = false

        var current: Token
        while (lexer.hasNext()) {
            current = lexer.next()
            println(current.toString().replace("\n", "\\n"))

            if (name == null && (current.type == Tokens.STRINGLITERAL || current.type == Tokens.STRINGQUOTED)) {
                name = current.data
                continue
            }
            if (name != null && current.type == Tokens.SPLITTER) {
                seenColon = true
                continue
            }
            if (name != null && !started && (current.type == Tokens.STRINGQUOTED || current.type == Tokens.STRINGLITERAL || current.type == Tokens.NUMBER || current.type == Tokens.BOOLEAN)) {
                if (seenColon) {
                    type = NodeType.fromToken(current.type)
                    value = current.data
                }
            }
            if (name != null && !started && seenColon && current.type == Tokens.OPENER && current.data == "[") {
                type = NodeType.ARRAY
                started = true
                value = ""
            }
            if (name != null && started && type == NodeType.ARRAY && ((current.type == Tokens.STRINGQUOTED || current.type == Tokens.STRINGLITERAL || current.type == Tokens.NUMBER || current.type == Tokens.BOOLEAN))) { //todo add another enum inside them to categorize into their sub type so like VALUE would be both str, number, bool
                if (value == "")
                    arrayType = current.type
                if (current.type != arrayType)
                    if (!((current.type == Tokens.STRINGLITERAL && arrayType == Tokens.STRINGQUOTED) || (current.type == Tokens.STRINGQUOTED && arrayType == Tokens.STRINGLITERAL)))
                        throw IllegalArgumentException("Arrays must contain all the same type!")
                value += "${current.data}φ"
            }

            if (type == NodeType.ARRAY && started && current.type == Tokens.CLOSER && current.data == "]" && value != null && name != null && arrayType != null) { //todo combine closer and opener since I always check data anyway
                val toAdd = type.toNode(name, mutableListOf(), parent.asNode(), "${(if (arrayType == Tokens.BOOLEAN) NodeType.PRIMITIVE_BOOLEAN else if (arrayType == Tokens.NUMBER) NodeType.PRIMITIVE_NUMBER else NodeType.PRIMITIVE_STRING).name}=$value")
                println("Adding $toAdd")
                parent.add(node = toAdd) //todo same as below, they can be combine just need different conditional for it.
                type = NodeType.UNKNOWN
                arrayType = null
                name = null
                value = null
                seenColon = false
                started = false
            }
            if (type != NodeType.UNKNOWN && type != NodeType.ARRAY && name != null && value != null) {
                val toAdd = type.toNode(name, mutableListOf(), parent.asNode(), value)
                println("Adding $toAdd")
                parent.add(node = toAdd)
                type = NodeType.UNKNOWN
                name = null
                value = null
                seenColon = false
            }


        }

        return if (parent is OrphanNode)
            parent else throw IllegalStateException("Detected end of file before expected!")
    } //todo store NodeType inside token so i can do current.type.nodeType

    private enum class NodeType {
        CATEGORY {
            override fun toNode(id: String, comments: MutableList<String>, parent: Node, value: String): Node {
                TODO("not implemented")
            }

        },
        ARRAY {
            override fun toNode(id: String, comments: MutableList<String>, parent: Node, value: String): Node {
                // value = TYPE=
                // value = STRING="test","test2","etc"
                // value = NUMBER=1,2,3,4,5,6,7,8,9
                // value = BOOLEAN=true,true,false,false,true
                val arr = ArrayNode(id, comments, parent)
                val type = NodeType.fromString(value.split("=")[0])
                for (str in value.substring(0, value.length-1).substring(value.indexOf('=') + 1).split("φ"))
                    arr.add(type.toNode("_array", mutableListOf(), arr, str).getAsPrimitive()!!) //todo remove !!
                return arr
            }
        },
        PRIMITIVE_STRING {
            override fun toNode(id: String, comments: MutableList<String>, parent: Node, value: String) = StringNode(id, comments, parent, value)
        },
        PRIMITIVE_NUMBER {
            override fun toNode(id: String, comments: MutableList<String>, parent: Node, value: String): Node = NumberNode(id, comments, parent, NumberFormat.getNumberInstance().parse(value))
        },
        PRIMITIVE_BOOLEAN {
            override fun toNode(id: String, comments: MutableList<String>, parent: Node, value: String): Node = BooleanNode(id, comments, parent, value.toBoolean())
        },
        UNKNOWN {
            override fun toNode(id: String, comments: MutableList<String>, parent: Node, value: String) = throw IllegalStateException("UNKNOWN is not a valid node type.")
        };

        abstract fun toNode(id: String, comments: MutableList<String>, parent: Node, value: String): Node
        companion object {
            fun fromToken(type: Tokens): NodeType = when (type) {
                    Tokens.NUMBER -> PRIMITIVE_NUMBER
                    Tokens.BOOLEAN -> PRIMITIVE_BOOLEAN
                    Tokens.STRINGLITERAL -> PRIMITIVE_STRING
                    Tokens.STRINGQUOTED -> PRIMITIVE_STRING
                    else -> UNKNOWN
                }
            fun fromString(name: String): NodeType {
                for (type in values())
                    if (type.name == name)
                        return type
                return UNKNOWN
            }
        }
    }
}