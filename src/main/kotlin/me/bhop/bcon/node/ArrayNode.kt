package me.bhop.bcon.node

class ArrayNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node) : ChildNode(id, comments, parent), MutableList<PrimitiveNode> by object : ArrayList<PrimitiveNode>() {

}