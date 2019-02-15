@file:Suppress("MemberVisibilityCanBePrivate")

package me.bhop.bcon.node

class ArrayNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node?, val elements: MutableList<PrimitiveNode> = mutableListOf()) : Node(id, comments, parent), Iterable<PrimitiveNode> { //todo could easily be based on a list of Node not PrimitiveNode to allow lists of objects.

    fun add(node: PrimitiveNode) {
        elements.add(node)
    }
    // todo other adds

    fun size(): Int = elements.size

    fun addAll(vararg nodes: PrimitiveNode) = elements.addAll(nodes)
    fun addAll(node: ArrayNode) = elements.addAll(node.elements)

    fun contains(node: Node): Boolean {
        return elements.contains(node.getAsPrimitive() ?: return false)
    }

    fun get(index: Int): PrimitiveNode? = elements[index]
    fun set(index: Int, node: PrimitiveNode) = elements.set(index, node)

    fun remove(index: Int): PrimitiveNode? = elements.removeAt(index)
    fun remove(node: Node): Boolean {
        return elements.remove(node.getAsPrimitive() ?: return false)
    }

    override fun iterator(): Iterator<PrimitiveNode> {
        return elements.iterator()
    }



}