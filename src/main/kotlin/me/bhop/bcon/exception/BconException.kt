package me.bhop.bcon.exception

sealed class BconException(message: String) : RuntimeException(message)

class TextParseException(message: String) : BconException(message)