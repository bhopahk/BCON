package me.bhop.bcon.node

class CategoryNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node?, children: MutableList<Node>? = mutableListOf()) : Node(id, comments, parent, children) {
    fun has(vararg path: String): Boolean = get(*path) == null

    fun get(vararg path: String): Node? {
        val target = if (path.isEmpty()) return this else children?.first { it.id == path[0] }
        return target?.getAsCategory()?.get(*path.copyOfRange(1, path.size))
    }

    fun add(vararg path: String, node: Node) {
        val target = if (path.isEmpty()) get(*path) else this
        if (target is CategoryNode)
            target.add(node = node)
    }
}