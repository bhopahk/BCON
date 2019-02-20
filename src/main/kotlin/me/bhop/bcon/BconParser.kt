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

            if (type != NodeType.UNKNOWN && name != null && value != null) {
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
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        },
        ARRAY {
            override fun toNode(id: String, comments: MutableList<String>, parent: Node, value: String): Node {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        }
    }
}