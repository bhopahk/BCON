package me.bhop.bcon.lexer

enum class Tokens(val pattern: String) {
    // Whitespace and comments first because they do not have conflicts with other elements.
    WHITESPACE("[ \t\r]+"),
    COMMENT("#[^\n]+"),

    // Values second because number and boolean need to override identifier
    NUMBER("[0-9]+"),
    BOOLEAN("true|false"),

    STRINGQUOTED("\"[^\"]*\""),
    STRINGLITERAL("[^: \\[\\]{}\n,]+"),

    // Key/Value organization can go last
    SPLITTER(":"),
    SEPARATOR("[,\n]"),
    OPENER("[{\\[]"),
    CLOSER("[}\\]]")
}