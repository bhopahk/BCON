package me.bhop.bcon.node

class NodeBuilder(
    private var id: String? = null,
    private val comments: MutableList<String> = mutableListOf(),
    private var parent: CategoryNode? = null
) {

    fun id(id: String) = apply { this.id = id }
    fun comment(vararg comment: String) = apply { this.comments.addAll(comment) }
    fun parent(node: CategoryNode?) = apply { this.parent = node }

    fun buildAsCategory(): CategoryNode {
        validate()
        return CategoryNode(id.orEmpty(), comments, parent)
    }
    fun buildAsArray(): ArrayNode {
        validate()
        return ArrayNode(id.orEmpty(), comments, parent)
    }
    fun buildAsPrimitive(value: String): PrimitiveNode {
        validate()
        return StringNode(id.orEmpty(), comments, parent, value)
    }
    fun buildAsPrimitive(value: Boolean): PrimitiveNode {
        validate()
        return BooleanNode(id.orEmpty(), comments, parent, value)
    }
    fun buildAsPrimitive(value: Number): PrimitiveNode {
        validate()
        return NumberNode(id.orEmpty(), comments, parent, value)
    }

    private fun validate() {
        if (id.isNullOrEmpty())
            throw IllegalStateException("Cannot build node with no id!")
        if (parent == null)
            throw IllegalStateException("Cannot build node with no parent!")
    }
}