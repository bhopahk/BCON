package me.bhop.bcon.io.lexer

import me.bhop.bcon.io.NodeType

enum class Tokens(val pattern: String, val type: NodeType, val primitive: Boolean) {
    // Whitespace and comments first because they do not have conflicts with other elements.
    WHITESPACE("[ \t\r]+", NodeType.UNKNOWN, false),
    COMMENT("#[^\\n]+|\\/\\/[^\\n]+", NodeType.UNKNOWN, false),

    // Values second because number and boolean need to override identifier
    NUMBER("[0-9]+", NodeType.PRIMITIVE_NUMBER, true),
    BOOLEAN("true|false", NodeType.PRIMITIVE_BOOLEAN, true),

    STRINGQUOTED("\"[^\"]*\"", NodeType.PRIMITIVE_STRING, true),
    STRINGLITERAL("[^: \\[\\]{}\n,\r.]+", NodeType.PRIMITIVE_STRING, true),

    // Key/Value organization can go last
    SPLITTER(":|\\.", NodeType.UNKNOWN, false),
    SEPARATOR("[,\n]", NodeType.UNKNOWN, false),
    DOOR("[}\\]]|[{\\[]", NodeType.UNKNOWN, false);
}