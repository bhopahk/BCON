package me.bhop.bcon.node

import kotlin.reflect.KClass

open class PrimitiveNode<T : Any> internal constructor(id: String, comments: MutableList<String> = mutableListOf(), parent: Node?, val value: T) : Node(id, comments, parent) {
    fun getType(): KClass<out T> = value::class
}