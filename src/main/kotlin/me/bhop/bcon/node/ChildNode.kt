package me.bhop.bcon.node

import kotlin.reflect.KProperty

abstract class ChildNode(id: String, comments: MutableList<String> = mutableListOf(), parent: Node) : Node(id, comments) {
    var parent: Node by ParentDelegate(parent)

    fun isAtRoot(): Boolean = parent is OrphanNode

    private class ParentDelegate(var parent: Node) {
        operator fun getValue(ref: Any?, property: KProperty<*>): Node {
            return parent
        }

        operator fun setValue(ref: Any?, property: KProperty<*>, value: Node) {
            if (value !is ParentNode)
                throw IllegalArgumentException("New parent must be of node type category!")
            if (ref !is Node)
                throw IllegalArgumentException("This delegate is being misused.")
            (parent as ParentNode).children.remove(ref)
            value.children.add(ref)
            parent = value
        }
    }
}