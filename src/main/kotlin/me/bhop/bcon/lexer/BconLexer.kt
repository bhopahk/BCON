package me.bhop.bcon.lexer

import java.io.BufferedReader
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors

class BconLexer(private val input: String, private var indexer: Boolean = true) : Iterator<Token> {
    constructor(reader: BufferedReader, indexer: Boolean = true) : this(reader.lines().collect(Collectors.joining("\n")), indexer)
    private val tokens: MutableList<Token> = mutableListOf()
    private var cursor = 0

    override fun hasNext(): Boolean = cursor < tokens.size

    override fun next(): Token = tokens[cursor++]

    fun previous(): Token = tokens[cursor--]

    fun peekAhead(): Token = tokens[cursor + 1]

    fun peekBehind(): Token = tokens[cursor - 1]

    fun lex() {
        cursor = 0
        tokens.clear()

        // Build the pattern from Tokens
        var patternStr = ""
        for (type in Tokens.values())
            patternStr += "|(?<${type.name}>${type.pattern})"
        val pattern = Pattern.compile(patternStr.substring(1))

        // Matcher loop
        val matcher: Matcher = pattern.matcher(input)
        while (matcher.find()) {

            for (type in Tokens.values())
                if (matcher.group(type.name) != null)
                    tokens.add(Token(type, matcher.group(type.name), -1, -1))
        }
    }
}