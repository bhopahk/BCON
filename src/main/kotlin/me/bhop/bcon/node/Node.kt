package me.bhop.bcon.node

abstract class Node(val id: String, val comments: MutableList<String> = mutableListOf()) {

    fun getAsCategory(): CategoryNode? = if (this is CategoryNode) this else null
    fun getAsPrimitive(): PrimitiveNode? = if (this is PrimitiveNode) this else null
    fun getAsArray(): ArrayNode? = if (this is ArrayNode) this else null

    fun isOrphan(): Boolean = this is OrphanNode
    fun isCategory(): Boolean = this is CategoryNode
    fun isPrimitive(): Boolean = this is PrimitiveNode
    fun isArray(): Boolean = this is ArrayNode




}
