package me.bhop.bcon.old

import me.bhop.bcon.node.*
import java.nio.file.Path
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors

//todo can use with(writer) when writing to the config
class BconReader {
    fun parseFile(path: Path): OrphanNode {
        return OrphanNode()
    }

    fun parseCategory(parent: ParentNode, categoryStr: String) {
        val categories: MutableList<String> = mutableListOf()
        val quotes: MutableList<String> = mutableListOf()
        val comments: MutableList<String> = mutableListOf()
        val arrays: MutableList<String> = mutableListOf()

        var category = categoryStr
        if (category.startsWith('{'))
            category = category.substring(1, category.length-1)

        // Start Category Removal
        var depth = 0
        var cStart = category.indexOf('{')
        var pointer = cStart + 1
        while (category.indexOf('{') != -1) {
            val open = category.indexOf('{', pointer)
            val close = category.indexOf('}', pointer)

            if (open != -1 && open < close) {
                depth++
                pointer = open + 1
                continue
            } else if (depth > 0) {
                depth--
                pointer = close + 1
                continue
            }

            categories.add(category.substring(cStart, close + 1))

            val before = category.substring(0, cStart).trim()
            val after = category.substring(close + 1)
            category = "$before${if (before[before.length-1] == ':') "" else ":"}${(categories.size-1).placeholderOf("\u03F5")} $after"
            depth = 0
            cStart = category.indexOf('{')
            pointer = cStart + 1
        }
        // End Category Removal

        // Start Quoted String Removal
        var noquote = ""
        var cursor = 0
        val matcher: Matcher = Pattern.compile("(\"(.*?)\")").matcher(category)
        while (matcher.find()) {
            noquote += category.substring(cursor, matcher.start())
            noquote += quotes.size.placeholderOf("\u03F6")
            cursor = matcher.start()
            quotes.add(category.substring(cursor, matcher.end()))
            cursor = matcher.end()
        }
        category = "$noquote${category.substring(cursor, category.length)}"

        // End Quoted String Removal

        // Start Comment Removal
        for (line in category.split("\n")) {
            val c = line.split("#")
            if (c.size >= 2) {
                val comment = c.stream().skip(1).collect(Collectors.joining("#"))
                comments.add(comment)
                category = category.replace(comment, (comments.size - 1).placeholderOf("\u0394"))
            }
        }
        // End Comment Removal

        // Start Array Removal
        while (category.contains('[')) {
            val open = category.indexOf('[')
            val close = category.indexOf(']')
            arrays.add(category.substring(open, close + 1))

            val start = category.substring(0, open).trim()
            val end = category.substring(close + 1)
            category = "$start${if (start[start.length - 1] == ':') "" else ":"}${(arrays.size - 1).placeholderOf("\u03B1")}$end"
        }
        // End Array Removal

        // Start Remaining Normalization
        while (category.contains(": "))
            category = category.replace(": ", ":")
        while (category.contains("\n\n"))
            category = category.replace("\n\n", "\n")
        while (category.contains(", "))
            category = category.replace(", ", "\n")
        // End Remaining Normalization

        // Start Insert Quote Placeholders
        for (i in 0 until quotes.size)
            category = category.replace(i.placeholderOf("\u03F6"), quotes[i])
        // End Insert Quote Placeholders

        val lines = category.split("\n")
        for (i in 0 until lines.size) {
            val line = lines[i]
            if (line.startsWith("#"))
                continue
            val lineComments: MutableList<String> = mutableListOf()
            var j = 1
            while ((i - j) >= 0 && lines[i - j].startsWith("#")) {
                lineComments.add(comments[Integer.valueOf(lines[i - j].replace("#\u0394", "").trim())])
                j++
            }

            val lineParts = line.split(":")
            if (lineParts[0].contains(".")) {
                val altParts = lineParts[0].split(".")
                var alt = ""
                for (comment in lineComments)
                    alt += "$comment\n"
                for (k in 1 until altParts.size)
                    alt += "${altParts[k]}."
                if (alt[alt.length - 1] == '.')
                    alt = alt.substring(0, alt.length - 1)
                alt += ":"
                alt += if (lineParts[1].contains("\u03F5"))
                    categories[Integer.valueOf(lineParts[1].replace("\u03F5", "").trim())]
                else
                    lineParts[1]
                val child = CategoryNode(id = altParts[0].trim(), parent = parent.asNode())
                parseCategory(child, alt)
                parent.add(node = child)
                continue
            }

            if (lineParts.size != 2)
                continue

            if (lineParts[1][0] == "\u03F5"[0]) {
                val child = CategoryNode(lineParts[0].trim(), lineComments, parent.asNode())
                parseCategory(child, categories[Integer.parseInt(lineParts[1].replace("\u03F5", "").trim())])
                parent.add(node = child)
            } else if (lineParts[1][0] == "\u03B1"[0]) {
                val array = ArrayNode(lineParts[0].trim(), lineComments, parent.asNode())
                val arrayStr = arrays[Integer.parseInt(lineParts[1].trim().replace("\u03B1", ""))]
                    .replace("[", "")
                    .replace("]", "")
                    .replace(", ", "\n")
                    .replace(",", "")
                for (arrLine in arrayStr.split("\n")) {
                    var ln = arrLine.trim()
                    if (ln.isEmpty())
                        continue
                    if (ln.contains("\u03F6"))
                        ln = quotes[Integer.parseInt(ln.replace("\u03F6", ""))].replace("\"", "")
                    array.add(ln.toPrimitiveNode("_array", mutableListOf(), array))
                }
                parent.add(node = array)
            } else
                parent.add(node = lineParts[1].toPrimitiveNode(lineParts[0].trim(), lineComments, parent.asNode()))
        }
    }

    private fun Int.placeholderOf(prefix: String): String {
        var placeholder = prefix
        for (i in this.toString().length .. 2)
            placeholder += "0"
        return "$placeholder${this}"
    }

    private fun String.toPrimitiveNode(id: String, comments: MutableList<String>, parent: Node): PrimitiveNode {
        return StringNode(id, comments, parent, this)
    }
}