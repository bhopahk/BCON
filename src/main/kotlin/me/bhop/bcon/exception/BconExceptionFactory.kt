package me.bhop.bcon.exception

import me.bhop.bcon.io.lexer.Token

object BconExceptionFactory {
    fun newParseException(text: String, message: String, fileName: String?, token: Token): TextParseException = BconExceptionFactory.newParseException(text, message, fileName, token.line, token.column)

    fun newParseException(text: String, message: String, fileName: String?, line: Int, column: Int): TextParseException {
        val lnLen = "$line".length
        var msg = ""
        msg += if (fileName != null) "----($fileName) $message\n" else "-----$message\n"
        if (line - 2 > 0)
            msg += "${(line -  2).toLn(lnLen)}${text.getLine(line - 2)}\n"
        if (line - 1 > 0)
            msg += "${(line -  1).toLn(lnLen)}${text.getLine(line - 1)}\n"
        msg += "${line.toLn(lnLen)}${text.getLine(line)}\n"
        var arrow = ""
        for (i in 0 .. (column + lnLen + 1))
            arrow += "-"
        return TextParseException("\n$msg$arrow^")
    }

    private fun String.getLine(number: Int): String = this.split("\n")[number - 1]

    private fun Int.toLn(len: Int): String {
        var msg = "$this"
        for (i in msg.length .. len)
            msg += " "
        return "$msg| "
    }
}