package me.bhop.bcon.lexer

data class Token(val type: Tokens, val data: String, val line: Int, val char: Int)