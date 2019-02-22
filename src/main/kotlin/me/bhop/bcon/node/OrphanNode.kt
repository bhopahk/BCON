package me.bhop.bcon.node

class OrphanNode(override val children: MutableList<Node> = mutableListOf()) : Node("_root"), ParentNode {
    override fun asNode(): Node = this

    fun toCategory(id: String, comments: MutableList<String>, parent: ParentNode): CategoryNode {
        val new = CategoryNode(id, comments, parent.asNode(), children)
        parent.add(node = new)
        return new
    }
}