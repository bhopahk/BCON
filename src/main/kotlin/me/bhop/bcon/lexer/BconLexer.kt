package me.bhop.bcon.lexer

import java.io.BufferedReader
import java.util.stream.Collectors

class BconLexer(val input: String, var indexer: Boolean = true) {
    constructor(reader: BufferedReader, indexer: Boolean = true) : this(reader.lines().collect(Collectors.joining("\n")), indexer)

//    fun lex(): List<Token> {
//
//    }


}