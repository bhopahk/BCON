package me.bhop.bcon.io

import me.bhop.bcon.node.*
import java.io.BufferedWriter
import java.nio.file.Files
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.*

class BconWriter( //todo maybe allow a custom comment format like "#{comment}" or "// {comment}"
    private val prettyPrinting: Boolean = false, //todo semi done
    private val printComments: Boolean = true,
    private val printGeneratedHeader: Boolean = true //todo include date generated and maybe some more info like bcon version idk - only if header is not included on OrphanNode
) { //todo global type adapters as well as local ones for a Bcon instance

    fun toBcon(node: Node, path: Path) {
        Files.newBufferedWriter(path).use { toBcon(node, it) }
    }

    fun toBcon(node: Node, writer: BufferedWriter) {
        with(writer) {
            write(toBcon(node))
        }
    }

    fun toBcon(node: Node): String {
        var str = ""
        if (printGeneratedHeader)
            str += "# ${SimpleDateFormat("EEEE MMMM dd, yyyy HH:mm:ss").format(Date())}\n"
        str += if (node is OrphanNode) {
            var string = ""
            for (child in node.children)
                string += toBcon(child, "")
            string
        } else
            toBcon(node, "")
        str = str.trim()
        if (str[str.length - 1] == ',')
            str = str.substring(0, str.length - 1)
        return str
    }

    private fun toBcon(node: Node, indent: String = ""): String {
        var str = ""
        if (printComments && prettyPrinting)
            for (comment in node.comments)
                str += "$indent# $comment\n"
        when (node) {
            is PrimitiveNode -> str += "$indent${node.id.asId()}: $node${if (prettyPrinting) "\n" else ", "}"
            is ArrayNode -> {
                str += "$indent${node.id.asId()}: ["
                for (element in node)
                    str += "$element,"
                if (str.endsWith(","))
                    str = str.substring(0, str.length - 1)
                str += "]${if (prettyPrinting) "\n" else ", "}"
            }
            is ParentNode -> {
                str += "$indent${node.id.asId()} {${if (prettyPrinting) "\n" else ""}"
                for (child in node.children)
                    str += toBcon(child, "$indent${if (prettyPrinting) "\t" else ""}")
                str = str.trim()
                if (str[str.length - 1] == ',')
                    str = str.substring(0, str.length - 1)
                str += "$indent}${if (prettyPrinting) "\n" else ", "}"
            }
            else -> throw IllegalStateException("Did I miss a node type??")
        }
//        str = str.trim()
//        if (str[str.length - 1] == ',')
//            str = str.substring(0, str.length - 1)
        return str
    }

    private fun String.asId(): String = if (this.contains(" ")) "\"$this\"" else this
}