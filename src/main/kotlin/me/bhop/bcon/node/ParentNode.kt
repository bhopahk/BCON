package me.bhop.bcon.node

interface ParentNode {
    val children: MutableList<Node>

    fun has(vararg path: String): Boolean = get(*path) == null

    fun get(vararg path: String): Node? {
        val target = if (path.isEmpty()) return asNode() else children.first { it.id == path[0] }
        return target.getAsCategory()?.get(*path.copyOfRange(1, path.size))
    }

    fun add(vararg path: String, node: Node) {
        var target = if (path.isEmpty()) asNode() else get(*path)
        if (target == null) {
            target = CategoryNode(path[0], parent = asNode())
            children.add(target)
        } //todo add conversion from category to orphan
        if (target is CategoryNode)
            target.add(node = node)
    }

    fun addByBuilder(): NodeBuilder = NodeBuilder(parent = asNode())

    fun children(): List<Node> = ArrayList(children)

    fun asNode(): Node
}