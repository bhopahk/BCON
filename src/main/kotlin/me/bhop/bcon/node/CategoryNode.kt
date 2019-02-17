package me.bhop.bcon.node

class CategoryNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node, override var children: MutableList<Node> = mutableListOf()) : ChildNode(id, comments, parent), ParentNode {
    override fun asNode(): Node = this

    fun toOrphanNode(): OrphanNode {
        parent.getAsCategory()?.children?.remove(this)
        return OrphanNode(children)
    }
}