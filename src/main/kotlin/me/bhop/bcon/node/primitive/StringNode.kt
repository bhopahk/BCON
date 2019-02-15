package me.bhop.bcon.node.primitive

import me.bhop.bcon.node.Node
import me.bhop.bcon.node.PrimitiveNode

class StringNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node?, value: String) : PrimitiveNode<String>(id, comments, parent, value)