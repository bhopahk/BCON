package me.bhop.bcon.node

sealed class PrimitiveNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node) : ChildNode(id, comments, parent) {

    open fun getAsString(): String? = null
    fun toStringNode(): StringNode? = if (this is StringNode) this else null
    fun isStringNode(): Boolean = this is StringNode

    open fun getAsBoolean(): Boolean? = null
    fun toBooleanNode(): BooleanNode? = if (this is BooleanNode) this else null
    fun isBooleanNode(): Boolean = this is BooleanNode

    open fun getAsNumber(): Number? = null
    fun toNumberNode(): NumberNode? = if (this is NumberNode) this else null
    fun isNumberNode(): Boolean = this is NumberNode

    open fun getAsInt(): Int? = null
    open fun getAsDouble(): Double? = null
    open fun getAsFloat(): Float? = null
    open fun getAsLong(): Long? = null
    open fun getAsShort(): Short? = null
}

class StringNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node, private val value: String) : PrimitiveNode(id, comments, parent) {
    override fun getAsString(): String? = value
    override fun toString(): String = "\"$value\""
}

class BooleanNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node, private val value: Boolean) : PrimitiveNode(id, comments, parent) {
    override fun getAsBoolean(): Boolean? = value
    override fun toString(): String = "$value"
}

class NumberNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node, private val value: Number) : PrimitiveNode(id, comments, parent) {
    override fun getAsNumber(): Number? = value
    override fun getAsInt(): Int? = value.toInt()
    override fun getAsDouble(): Double? = value.toDouble()
    override fun getAsFloat(): Float? = value.toFloat()
    override fun getAsShort(): Short? = value.toShort()
    override fun getAsLong(): Long? = value.toLong()
    override fun toString(): String = "$value"
}