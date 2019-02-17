package me.bhop.bcon.node

class OrphanNode(override val children: MutableList<Node> = mutableListOf()) : Node("_root"), ParentNode {
    override fun asNode(): Node = this
}