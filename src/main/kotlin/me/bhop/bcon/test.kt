package me.bhop.bcon

import me.bhop.bcon.node.CategoryNode
import me.bhop.bcon.node.ArrayNode
import me.bhop.bcon.node.PrimitiveNode
import me.bhop.bcon.node.StringNode

fun main(args: Array<String>) {
    val parent = CategoryNode("test", parent = null)

    val arr = ArrayNode("", parent = parent)
    arr.add(StringNode("coolname", parent = parent, value = "Test"))

    val idk: PrimitiveNode<*> = StringNode("c", parent = parent, value = "Test2")

    println("Idk Type: ${idk.getType() == String::class}")
    println("Idk Type: ${idk.value is String}")


}

