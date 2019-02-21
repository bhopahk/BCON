package me.bhop.bcon.io

import me.bhop.bcon.exception.BconExceptionFactory
import me.bhop.bcon.lexer.BconLexer
import me.bhop.bcon.lexer.Token
import me.bhop.bcon.lexer.Tokens
import me.bhop.bcon.node.CategoryNode
import me.bhop.bcon.node.OrphanNode
import me.bhop.bcon.node.ParentNode
import java.io.Reader
import java.util.stream.Collectors

class BconReader(
    private val pullComments: Boolean = true
) {

    fun fromBcon(reader: Reader, fileName: String? = null): OrphanNode = fromBcon(reader.readLines().stream().collect(Collectors.joining("\n")), fileName)

    fun fromBcon(text: String, fileName: String? = null): OrphanNode {
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
        var seenSeparator = false

        var current: Token
        loop@while (lexer.hasNext()) {
            current = lexer.next()
            when(current.type) {
                Tokens.WHITESPACE -> continue@loop
                Tokens.SEPARATOR -> {
                    seenSeparator = true
                    continue@loop
                }
                Tokens.COMMENT -> {
                    if (pullComments)
                        comments.add(current.data.substring(1).trim())
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
                        throw BconExceptionFactory.newParseException(text, "Detected illegal start of category - no associated key was found.", fileName, current)
                    } else if (current.data == "}") {
                        if (arrayType == null && type == NodeType.UNKNOWN && name == null && value == null && !seenColon) {
                            if (parent is CategoryNode) {
                                parent = parent.parent.getAsCategory() ?: throw BconExceptionFactory.newParseException(text, "Unknown Error.", fileName, current)
                                seenSeparator = false
                                continue@loop
                            }
                            throw BconExceptionFactory.newParseException(text, "Detected end of file before expected!", fileName, current)
                        }
                        throw BconExceptionFactory.newParseException(text, "Detected illegal end of category.", fileName, current)
                    } else if (current.data == "[") {
                        if (name != null && seenColon && current.type == Tokens.DOOR) {
                            type = NodeType.ARRAY
                            value = ""
                            continue@loop
                        }
                        throw BconExceptionFactory.newParseException(text, "Detected illegal start of array - no associated key was found.", fileName, current)
                    }
                }
                else -> {}
            }

            if (name == null && current.type.type == NodeType.PRIMITIVE_STRING) {
                if (seenSeparator) {
                    name = current.data
                    continue
                }
                throw  BconExceptionFactory.newParseException(text, "Detected attempted key declaration without comma or new line! Note, this could be due to a string value with a space but no quotes.", fileName, current)
            }
            if (type != NodeType.ARRAY && name != null && current.type.primitive) {
                if (seenColon) {
                    type = current.type.type
                    value = current.data
                } else
                    throw BconExceptionFactory.newParseException(text, "Detected value declaration without separator! (:)", fileName, current)
            }
            if (name != null && type == NodeType.ARRAY && current.type.primitive) {
                if (value == "")
                    arrayType = current.type
                if (current.type.type != arrayType?.type)
                    throw BconExceptionFactory.newParseException(text, "Detected array value mismatch! ${current.type.type.name} != ${arrayType?.type}", fileName, current)
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
                seenSeparator = false
                continue
            }
        }

        return if (parent is OrphanNode)
            parent else throw IllegalStateException("Detected end of file before expected!")
    }
}