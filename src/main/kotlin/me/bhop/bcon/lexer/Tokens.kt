package me.bhop.bcon.lexer

enum class Tokens(val pattern: String) {
    // Whitespace first b/c it doesnt matter
    WHITESPACE("[ \t\r]+"),

    // Values second because number and boolean need to override identifier
    NUMBER("[0-9]+"),
    BOOLEAN("true|false"),

    STRING_QUOTED("\"[^\"]*\""),
    STRING_LITERAL("[a-zA-Z0-9]+"),

    // Key/Value organization can go last

    SPLITTER(":"),
    SEPARATOR("[,\n]"),
    OPENER("[{\\[]"),
    CLOSER("[}\\]]")
}