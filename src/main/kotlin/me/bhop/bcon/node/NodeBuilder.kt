package me.bhop.bcon.node

class NodeBuilder(
    private var id: String? = null,
    private val comments: MutableList<String> = mutableListOf(),
    private var parent: Node? = null
) {

    fun id(id: String) = apply { this.id = id }
    fun comment(vararg comment: String) = apply { this.comments.addAll(comment) }
    fun parent(node: Node) = apply { this.parent = node }

    fun buildAsCategory(): CategoryNode {
        val nid = id ?: throw IllegalStateException("Cannot build node with no id!")
        val np = parent ?: throw IllegalStateException("Cannot build node with no parent!")
        return CategoryNode(nid, comments, np)
    }
    fun buildAsArray(): ArrayNode {
        val nid = id ?: throw IllegalStateException("Cannot build node with no id!")
        val np = parent ?: throw IllegalStateException("Cannot build node with no parent!")
        return ArrayNode(nid, comments, np)
    }
    fun buildAsPrimitive(value: String): PrimitiveNode {
        val nid = id ?: throw IllegalStateException("Cannot build node with no id!")
        val np = parent ?: throw IllegalStateException("Cannot build node with no parent!")
        return StringNode(nid, comments, np, value)
    }
    fun buildAsPrimitive(value: Boolean): PrimitiveNode {
        val nid = id ?: throw IllegalStateException("Cannot build node with no id!")
        val np = parent ?: throw IllegalStateException("Cannot build node with no parent!")
        return BooleanNode(nid, comments, np, value)
    }
    fun buildAsPrimitive(value: Number): PrimitiveNode {
        val nid = id ?: throw IllegalStateException("Cannot build node with no id!")
        val np = parent ?: throw IllegalStateException("Cannot build node with no parent!")
        return NumberNode(nid, comments, np, value)
    }
}