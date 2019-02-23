package me.bhop.bcon.io.lexer

import java.io.BufferedReader
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors

class BconLexer(private val input: String, var indexer: Boolean = true) : Iterator<Token> {
    constructor(reader: BufferedReader, indexer: Boolean = true) : this(reader.lines().collect(Collectors.joining("\n")), indexer)
    internal val tokens: MutableList<Token> = mutableListOf()
    private var cursor = -1

    override fun next(): Token = tokens[cursor++]
    override fun hasNext(): Boolean = cursor < tokens.size

    fun previous(): Token = tokens[cursor--]
    fun hasPrevious(): Boolean = cursor > 0

    fun peekAhead(amount: Int = 1): Token = tokens[cursor + amount]
    fun canPeekAhead(amount: Int = 1): Boolean = cursor + amount < tokens.size

    fun peekBehind(amount: Int = 2): Token = tokens[cursor - amount]
    fun canPeekBehind(amount: Int = 2): Boolean = cursor - amount > 0

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
            for (type in Tokens.values()) {
                var data = matcher.group(type.name)
                if (data != null) {
                    if (type == Tokens.STRINGQUOTED)
                        data = data.substring(1, data.length - 1)
                    val position = if (indexer) pos(matcher.start(type.name)) else Pair(-1, -1)
                    tokens.add(Token(type, data, position.first, position.second))
                }
            }
        }
    }

    private fun pos(index: Int): Pair<Int, Int> {
        var line = 1
        var col = 1
        var cursor = 0
        while (cursor <= index) {
            if (input[cursor] == '\n') {
                line++
                col = 0
            } else
                col++
            cursor++
        }
        return Pair(line, col)
    }
}