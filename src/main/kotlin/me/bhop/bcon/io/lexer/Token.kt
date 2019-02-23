package me.bhop.bcon.io.lexer

data class Token(val type: Tokens, val data: String, val line: Int, val column: Int)