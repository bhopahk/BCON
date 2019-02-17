package me.bhop.bcon.node

class CategoryNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node?, children: MutableList<Node>? = mutableListOf()) : Node(id, comments, parent, children) {
    fun has(vararg path: String): Boolean = get(*path) == null

    fun get(vararg path: String): Node? {
        val target = if (path.isEmpty()) return this else children?.first { it.id == path[0] }
        return target?.getAsCategory()?.get(*path.copyOfRange(1, path.size))
    }

    fun add(vararg path: String, node: Node) {
        var target = if (path.isEmpty()) this else get(*path)
        if (target == null) {
            target = CategoryNode(path[0], parent = this)
            children?.add(target)
        }
        if (target is CategoryNode)
            target.add(node = node)
    }

    fun add(): NodeBuilder = NodeBuilder(parent = this)

    fun children(): List<Node> = ArrayList(children)
}