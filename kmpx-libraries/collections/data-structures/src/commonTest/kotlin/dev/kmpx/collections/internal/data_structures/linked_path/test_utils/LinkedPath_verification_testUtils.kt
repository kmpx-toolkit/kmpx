package dev.kmpx.collections.internal.data_structures.linked_path.test_utils

import dev.kmpx.collections.internal.data_structures.linked_path.LinkedPath
import dev.kmpx.collections.internal.data_structures.linked_path.LinkedPath.Node

private fun <PayloadT> LinkedPath<PayloadT>.verifyContent(): List<Node<PayloadT>> {
    val headNode = this.headNode ?: return emptyList()

    val resultList = mutableListOf<Node<PayloadT>>()

    fun verifyNodeRecursively(
        sourceNode: Node<PayloadT>?,
        node: Node<PayloadT>,
    ) {
        if (node.previousNode != sourceNode) {
            throw IllegalStateException("Inconsistent previous node reference")
        }

        resultList.add(node)

        node.nextNode?.let {
            verifyNodeRecursively(node, it)
        }
    }

    verifyNodeRecursively(
        sourceNode = null,
        node = headNode,
    )

    return resultList
}

fun <PayloadT> LinkedPath<PayloadT>.verifyIntegrity(): List<PayloadT> {
    when {
        headNode != null && tailNode != null -> {
            // Consistent: List is non-empty
        }


        headNode == null && tailNode == null -> {
            // Consistent: List empty
        }

        else -> {
            throw IllegalStateException("Inconsistent head/tail nodes")
        }
    }

    val nodes = verifyContent()

    if (nodes.lastOrNull() != tailNode) {
        throw IllegalStateException("Inconsistent tail node reference")
    }

    return nodes.map { it.payload }
}
