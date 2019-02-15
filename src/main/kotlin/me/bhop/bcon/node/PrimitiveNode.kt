package me.bhop.bcon.node

import kotlin.reflect.KClass

sealed class PrimitiveNode<T : Any>(id: String, comments: MutableList<String> = mutableListOf(), parent: Node?, val value: T) : Node(id, comments, parent) {
    fun getType(): KClass<out T> = value::class
}

class StringNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node?, value: String) : PrimitiveNode<String>(id, comments, parent, value)
class BooleanNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node?, value: Boolean) : PrimitiveNode<Boolean>(id, comments, parent, value)
class IntNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node?, value: Int) : PrimitiveNode<Int>(id, comments, parent, value)
class DoubleNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node?, value: Double) : PrimitiveNode<Double>(id, comments, parent, value)
class LongNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node?, value: Long) : PrimitiveNode<Long>(id, comments, parent, value)
class ShortNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node?, value: Short) : PrimitiveNode<Short>(id, comments, parent, value)