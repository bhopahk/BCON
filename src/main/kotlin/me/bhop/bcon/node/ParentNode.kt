package me.bhop.bcon.node

interface ParentNode {
    val children: MutableList<Node>

    fun has(vararg path: String): Boolean = get(*path) == null

    fun get(vararg path: String, create: Boolean = false): Node? {
        var target = if (path.isEmpty()) return asNode() else children.firstOrNull { it.id == path[0] }
        if (target == null && create) {
            target = CategoryNode(path[0], parent = asNode())
            children.add(target)
        }
        if (target is CategoryNode)
            return target.getAsCategory()?.get(*path.copyOfRange(1, path.size), create = create)
        return target
    }

    fun add(vararg path: String, node: Node) {
        val target = if (path.isEmpty()) asNode() else get(*path, create = true)
        if (target is ParentNode) {
            val existing = target.get(node.id)
            if (existing is CategoryNode && node is CategoryNode)
                existing.children.addAll(node.children)
            else {
                if (existing != null)
                    target.children.remove(existing)
                target.children.add(node)
            }
        }
    }

    fun addByBuilder(): NodeBuilder = NodeBuilder(parent = asNode())

    fun children(): List<Node> = ArrayList(children)

    fun asNode(): Node
}