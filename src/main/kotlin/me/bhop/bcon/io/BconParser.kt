package me.bhop.bcon.io

import me.bhop.bcon.lexer.BconLexer
import me.bhop.bcon.lexer.Token
import me.bhop.bcon.lexer.Tokens
import me.bhop.bcon.node.CategoryNode
import me.bhop.bcon.node.OrphanNode
import me.bhop.bcon.node.ParentNode

class BconParser {


    fun fromBcon(text: String): OrphanNode {
//        val text = Files.newBufferedReader(Paths.get("./bcon.conf")).readText()
        val lexer = BconLexer(text)
        lexer.lex()

        // Current Parent
        var parent: ParentNode = OrphanNode()

        // Current Element being constructed
        var arrayType: Tokens? = null
        var type: NodeType = NodeType.UNKNOWN
        val comments: MutableList<String> = mutableListOf()
        val prefix: MutableList<String> = mutableListOf()
        var name: String? = null
        var value: String? = null
        var seenColon = false

        var current: Token
        loop@while (lexer.hasNext()) {
            current = lexer.next()
            when(current.type) {
                Tokens.WHITESPACE -> continue@loop
                Tokens.COMMENT -> {
                    println("Comment: $current")
                    comments.add(current.data)
                    continue@loop
                }
                Tokens.SPLITTER -> {
                    if (current.data == "." && name != null && !seenColon) {
                        prefix.add(name)
                        name = null
                        continue@loop
                    } else if (current.data == ":" && name != null) {
                        seenColon = true
                        continue@loop
                    }
                }
                Tokens.DOOR -> {
                    if (current.data == "{") {
                        if (arrayType == null && type == NodeType.UNKNOWN && name != null && value == null) {
                            val child = CategoryNode(name, ArrayList(comments), parent.asNode())
                            parent.add(*prefix.toTypedArray(), node = child)
                            parent = child
                            name = null
                            seenColon = false
                            prefix.clear()
                            comments.clear()
                            continue@loop
                        }
                        throw IllegalStateException("Detected invalid start of category!")
                    } else if (current.data == "}") {
                        if (arrayType == null && type == NodeType.UNKNOWN && name == null && value == null && !seenColon) {
                            if (parent is CategoryNode) {
                                parent = parent.parent.getAsCategory() ?: throw IllegalStateException("I dont think that this should happen!")
                                continue@loop
                            }
                            throw IllegalStateException("Detected end of file before expected!")
                        }
                        throw IllegalStateException("Detected end of category before expected!")
                    } else if (current.data == "[") {
                        if (name != null && seenColon && current.type == Tokens.DOOR) {
                            type = NodeType.ARRAY
                            value = ""
                            continue@loop
                        }
                        throw IllegalStateException("Detected invalid array opener!")
                    }
                }
                else -> {}
            }

            if (name == null && current.type.type == NodeType.PRIMITIVE_STRING) {
                name = current.data
                continue
            }
            if (type != NodeType.ARRAY && name != null && current.type.primitive) {
                if (seenColon) {
                    type = current.type.type
                    value = current.data
                }
            }
            if (name != null && type == NodeType.ARRAY && current.type.primitive) {
                if (value == "")
                    arrayType = current.type
                if (current.type.type != arrayType?.type)
                    throw IllegalArgumentException("Arrays must contain all the same type!")
                value += "${current.data}φ"
                continue
            }

            if (type != NodeType.UNKNOWN && name != null && value != null) {
                parent.add(node = if (type != NodeType.ARRAY) type.toNode(name, ArrayList(comments), parent.asNode(), value)
                else if (current.type == Tokens.DOOR && current.data == "]" && arrayType != null) type.toNode(name, ArrayList(comments), parent.asNode(), value, arrayType.type)
                else continue)

                type = NodeType.UNKNOWN
                arrayType = null
                name = null
                value = null
                seenColon = false
                prefix.clear()
                comments.clear()
                continue
            }

            /*if (current.type == Tokens.SPLITTER && current.data == "." && name != null && !seenColon) {
                prefix.add(name)
                name = null
                continue
            }*/

            /*if (current.type == Tokens.DOOR && current.data == "{") {
                if (arrayType == null && type == NodeType.UNKNOWN && name != null && value == null) {
                    val child = CategoryNode(name, mutableListOf(), parent.asNode())
                    parent.add(*prefix.toTypedArray(), node = child) //todo remove quotes from Strings (might be doable with regex by changing the group)
                    parent = child
                    name = null
                    seenColon = false
                    prefix.clear()
                    continue
                }
                throw IllegalStateException("Detected invalid start of category!")
            }
            if (current.type == Tokens.DOOR && current.data == "}") {
                if (arrayType == null && type == NodeType.UNKNOWN && name == null && value == null && !seenColon) {
                    if (parent is CategoryNode) {
                        parent = parent.parent.getAsCategory()!! //todo remove !!
                        continue
                    }
                    throw IllegalStateException("Detected end of file before expected!")
                }
                throw IllegalStateException("Detected end of category before expected!")
            }*/


            /*if (name != null && current.type == Tokens.SPLITTER && current.data == ":") {
                seenColon = true
                continue
            }*/

            /*if (name != null && seenColon && current.type == Tokens.DOOR && current.data == "[") {
                type = NodeType.ARRAY
                value = ""
                continue
            }*/
            if (name != null && type == NodeType.ARRAY && current.type.primitive) {
                if (value == "")
                    arrayType = current.type
                if (current.type != arrayType)
                    if (!((current.type == Tokens.STRINGLITERAL && arrayType == Tokens.STRINGQUOTED) || (current.type == Tokens.STRINGQUOTED && arrayType == Tokens.STRINGLITERAL)))
                        throw IllegalArgumentException("Arrays must contain all the same type!")
                value += "${current.data}φ"
                continue
            }

            if (type != NodeType.UNKNOWN && name != null && value != null) {
                parent.add(node = if (type != NodeType.ARRAY) type.toNode(name, mutableListOf(), parent.asNode(), value)
                    else if (current.type == Tokens.DOOR && current.data == "]" && arrayType != null) type.toNode(name, mutableListOf(), parent.asNode(), value, arrayType.type)
                    else continue)

                type = NodeType.UNKNOWN
                arrayType = null
                name = null
                value = null
                seenColon = false
                prefix.clear()
                continue
            }
        }

        return if (parent is OrphanNode)
            parent else throw IllegalStateException("Detected end of file before expected!")
    }
}