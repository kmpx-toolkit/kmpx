package dev.kmpx.collections.internal.data_structures.linked_path

import dev.kmpx.collections.internal.data_structures.linked_path.LinkedPath.Node

/**
 * Double-linked path of nodes, most commonly known as a double-linked list. To avoid clashing with standard library
 * names and to stress that we me mean the list _data structure_ (not a collection), we use the term "path" here, which
 * is taken from graph theory.
 */
class LinkedPath<PayloadT> private constructor() {
    class Node<PayloadT> internal constructor(
        initialPayload: PayloadT,
    ) {
        private var mutablePreviousNode: Node<PayloadT>? = null

        val previousNode: Node<PayloadT>?
            get() = mutablePreviousNode

        internal fun setPreviousNode(
            node: Node<PayloadT>?,
        ) {
            mutablePreviousNode = node
        }

        private var mutableNextNode: Node<PayloadT>? = null

        val nextNode: Node<PayloadT>?
            get() = mutableNextNode

        internal fun setNextNode(
            node: Node<PayloadT>?,
        ) {
            mutableNextNode = node
        }

        private var mutablePayload: PayloadT = initialPayload

        val payload: PayloadT
            get() = mutablePayload

        fun setPayload(
            payload: PayloadT,
        ) {
            mutablePayload = payload
        }
    }

    companion object {
        fun <Payload> create(): LinkedPath<Payload> = LinkedPath()
    }

    private var mutableHeadNode: Node<PayloadT>? = null

    val headNode: Node<PayloadT>?
        get() = mutableHeadNode

    private var mutableTailNode: Node<PayloadT>? = null

    val tailNode: Node<PayloadT>?
        get() = mutableTailNode

    fun prepend(
        payload: PayloadT,
    ): Node<PayloadT> {
        val newNode = Node(
            initialPayload = payload,
        )

        val currentHead = mutableHeadNode

        if (currentHead == null) {
            // Empty list: head and tail become the new node
            mutableHeadNode = newNode
            mutableTailNode = newNode
        } else {
            // Non-empty: link before current head
            newNode.setNextNode(currentHead)
            currentHead.setPreviousNode(newNode)

            mutableHeadNode = newNode
        }

        return newNode
    }

    fun append(
        payload: PayloadT,
    ): Node<PayloadT> {
        val newNode = Node(
            initialPayload = payload,
        )

        val currentTail = mutableTailNode

        if (currentTail == null) {
            // Empty list: head and tail become the new node
            mutableHeadNode = newNode
            mutableTailNode = newNode
        } else {
            // Non-empty: link after current tail
            newNode.setPreviousNode(currentTail)
            currentTail.setNextNode(newNode)

            mutableTailNode = newNode
        }

        return newNode
    }

    fun appendRelinking(
        linkedPath: LinkedPath<PayloadT>,
    ) {
        val otherHeadNode = linkedPath.headNode ?: return
        val otherTailNode = linkedPath.tailNode ?: return

        val currentTailNode = this.mutableTailNode

        if (currentTailNode == null) {
            // Current path is empty: just take over the other path
            this.mutableHeadNode = otherHeadNode
            this.mutableTailNode = otherTailNode
        } else {
            // Current path is non-empty: link the other path after current tail
            currentTailNode.setNextNode(otherHeadNode)
            otherHeadNode.setPreviousNode(currentTailNode)

            this.mutableTailNode = otherTailNode
        }

        // Detach the other path completely
        linkedPath.mutableHeadNode = null
        linkedPath.mutableTailNode = null
    }

    fun insertBefore(
        referenceNode: Node<PayloadT>,
        payload: PayloadT,
    ): Node<PayloadT> {
        val newNode = Node(
            initialPayload = payload,
        )

        val previousNode = referenceNode.previousNode

        // Link new node between previousNode and referenceNode

        newNode.setPreviousNode(previousNode)
        newNode.setNextNode(referenceNode)

        referenceNode.setPreviousNode(newNode)

        if (previousNode != null) {
            previousNode.setNextNode(newNode)
        } else {
            // Inserting before current head
            mutableHeadNode = newNode
        }

        return newNode
    }

    fun insertAfter(
        referenceNode: Node<PayloadT>,
        payload: PayloadT,
    ): Node<PayloadT> {
        val newNode = Node(payload)
        val nextNode = referenceNode.nextNode

        // Link new node between referenceNode and next

        newNode.setNextNode(nextNode)
        newNode.setPreviousNode(referenceNode)

        referenceNode.setNextNode(newNode)

        if (nextNode != null) {
            nextNode.setPreviousNode(newNode)
        } else {
            // Inserting after current tail
            mutableTailNode = newNode
        }

        return newNode
    }

    fun cutOff(
        node: Node<PayloadT>,
    ) {
        val previousNode = node.previousNode
        val nextNode = node.nextNode

        // Bridge neighbours
        if (previousNode != null) {
            previousNode.setNextNode(nextNode)
        } else {
            // Cutting off the head

            if (node != mutableHeadNode) { // Best-effort check
                throw IllegalStateException("Node to cut off is not connected to the linked path.")
            }

            mutableHeadNode = nextNode
        }

        if (nextNode != null) {
            nextNode.setPreviousNode(previousNode)
        } else {
            // Cutting off the tail

            if (node != mutableTailNode) { // Best-effort check
                throw IllegalStateException("Node to cut off is not connected to the linked path.")
            }

            mutableTailNode = previousNode
        }

        // Detach node completely
        node.setPreviousNode(null)
        node.setNextNode(null)
    }
}

fun <PayloadT> LinkedPath<PayloadT>.traverse(): Sequence<Node<PayloadT>> {
    val headNode = this.headNode ?: return emptySequence()

    return generateSequence(
        headNode,
    ) { node ->
        node.nextNode
    }
}

fun <PayloadT> LinkedPath<PayloadT>.insertAllBefore(
    referenceNode: Node<PayloadT>,
    payloads: Iterable<PayloadT>,
) {
    val payloadIterator = payloads.iterator()

    if (!payloadIterator.hasNext()) return

    val firstPayload = payloadIterator.next()

    val insertedNode = insertBefore(
        referenceNode = referenceNode,
        payload = firstPayload,
    )

    insertAllAfterRecursive(
        referenceNode = insertedNode,
        payloadIterator = payloadIterator,
    )
}

fun <PayloadT> LinkedPath<PayloadT>.insertAllAfter(
    referenceNode: Node<PayloadT>,
    payloads: Iterable<PayloadT>,
) {
    val payloadIterator = payloads.iterator()

    insertAllAfterRecursive(
        referenceNode = referenceNode,
        payloadIterator = payloadIterator,
    )
}

private tailrec fun <PayloadT> LinkedPath<PayloadT>.insertAllAfterRecursive(
    referenceNode: Node<PayloadT>,
    payloadIterator: Iterator<PayloadT>,
) {
    if (!payloadIterator.hasNext()) return

    val nextPayload = payloadIterator.next()

    val insertedNode = insertAfter(
        referenceNode = referenceNode,
        payload = nextPayload,
    )

    insertAllAfterRecursive(
        referenceNode = insertedNode,
        payloadIterator = payloadIterator,
    )
}

fun <PayloadT> LinkedPath<PayloadT>.appendAll(
    payloads: Iterable<PayloadT>,
) {
    val payloadIterator = payloads.iterator()

    appendAllRecursive(
        payloadIterator = payloadIterator,
    )
}

private tailrec fun <PayloadT> LinkedPath<PayloadT>.appendAllRecursive(
    payloadIterator: Iterator<PayloadT>,
) {
    if (!payloadIterator.hasNext()) return

    val nextPayload = payloadIterator.next()

    append(
        payload = nextPayload,
    )

    appendAllRecursive(
        payloadIterator = payloadIterator,
    )
}

fun <PayloadT> LinkedPath<PayloadT>.getNodeAt(
    index: Int,
): Node<PayloadT> = this.traverse().elementAt(index)
