package me.bhop.bcon

import me.bhop.bcon.node.*

fun main(args: Array<String>) {
    val parent: Node = OrphanNode()

    val arr = ArrayNode("", parent = parent)
    arr.add(StringNode("coolname", parent = parent, value = "Test"))

    val idk: PrimitiveNode = StringNode("c", parent = parent, value = "Test2")

    val b: NodeBuilder = NodeBuilder().id("")

    val x: OrphanNode


}

