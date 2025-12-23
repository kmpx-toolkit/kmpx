package dev.kmpx.collections.multi_sets

/**
 * [MultiSet] implementation internally based on a [HashMap].
 *
 * @param E the type of elements contained in the collection
 */
class HashMultiSet<E>() : AbstractMutableMultiSet<E>() {
    private class MutableCount {
        private var _count = 0

        fun get(): Int = _count

        fun increase(delta: Int = 1) {
            _count += delta
        }

        fun decrease(delta: Int = 1) {
            _count = (_count - delta).coerceAtLeast(0)
        }
    }

    private data class EntryImpl<E>(
        override val element: E,
        override val count: Int,
    ) : AbstractEntry<E>()

    private inner class EntrySetImpl : AbstractSet<MultiSet.Entry<E>>() {
        private inner class IteratorImpl(
            private val innerIterator: Iterator<Map.Entry<E, MutableCount>>,
        ) : Iterator<MultiSet.Entry<E>> {
            override fun next(): MultiSet.Entry<E> {
                val nextInnerEntry = innerIterator.next()

                return EntryImpl(
                    element = nextInnerEntry.key,
                    count = nextInnerEntry.value.get(),
                )
            }

            override fun hasNext(): Boolean = innerIterator.hasNext()
        }

        override val size: Int
            get() = _mutableCountByElement.size

        override fun iterator(): Iterator<MultiSet.Entry<E>> = IteratorImpl(
            innerIterator = _mutableCountByElement.entries.iterator(),
        )
    }

    private inner class MutableIteratorImpl(
        private val innerEntryIterator: MutableIterator<Map.Entry<E, MutableCount>>,
    ) : MutableIterator<E> {
        /**
         * The entry currently being visited by the iterator.
         */
        private var _currentInnerEntry: Map.Entry<E, MutableCount>? = null

        /**
         * The number of remaining visits for the currently visited entry.
         */
        private var _remainingVisitCount = 0

        /**
         * A flag indicating whether [remove] has been called after the last [next].
         */
        private var _wasRemoved = false

        override fun remove() {
            if (_wasRemoved) {
                throw IllegalStateException("remove() has already been called after the last next()")
            }

            when (val currentlyVisitedEntry = _currentInnerEntry) {
                null -> {
                    throw IllegalStateException("next() has not been called yet")
                }

                else -> {
                    val (_, mutableCount) = currentlyVisitedEntry

                    if (mutableCount.get() == 0) {
                        throw ConcurrentModificationException("Element count is already zero")
                    }

                    mutableCount.decrease()

                    if (mutableCount.get() == 0) {
                        innerEntryIterator.remove()

                        _currentInnerEntry = null
                    }

                    _size -= 1
                    _wasRemoved = true
                }
            }
        }

        override fun next(): E {
            val nextElement = when (val currentlyVisitedEntry = _currentInnerEntry) {
                null -> {
                    nextFromNextEntry()
                }

                else -> {
                    val (element, mutableCount) = currentlyVisitedEntry

                    if (mutableCount.get() < _remainingVisitCount) {
                        throw ConcurrentModificationException("Element count has been modified concurrently")
                    }

                    if (_remainingVisitCount > 0) {
                        _remainingVisitCount -= 1

                        element
                    } else {
                        nextFromNextEntry()
                    }
                }
            }

            _wasRemoved = false

            return nextElement
        }

        override fun hasNext(): Boolean = when (_currentInnerEntry) {
            null -> innerEntryIterator.hasNext()

            else -> when {
                _remainingVisitCount > 0 -> true
                else -> innerEntryIterator.hasNext()
            }
        }

        private fun nextFromNextEntry(): E {
            val nextEntry = innerEntryIterator.next()
            val (element, mutableCount) = nextEntry

            val initialCount = mutableCount.get()

            if (initialCount == 0) {
                throw IllegalStateException("Element count is zero")
            }

            _currentInnerEntry = nextEntry
            _remainingVisitCount = initialCount - 1

            return element
        }
    }

    companion object {
        fun <E> of(
            elements: Array<out E>,
        ): MutableMultiSet<E> = HashMultiSet<E>().apply {
            elements.forEach {
                add(it)
            }
        }

        fun <E> of(
            elements: Iterable<E>,
        ): MutableMultiSet<E> = HashMultiSet<E>().apply {
            elements.forEach {
                add(it)
            }
        }
    }

    private val _mutableCountByElement: MutableMap<E, MutableCount> = HashMap()

    override val entrySet: Set<MultiSet.Entry<E>> = EntrySetImpl()

    private var _size = 0

    override val size: Int
        get() = _size

    override fun iterator(): MutableIterator<E> = MutableIteratorImpl(
        innerEntryIterator = _mutableCountByElement.entries.iterator(),
    )

    override fun add(element: E): Boolean {
        val mutableCount = _mutableCountByElement.getOrPut(element) {
            MutableCount()
        }

        mutableCount.increase()

        _size += 1

        return true
    }

    override fun add(element: E, count: Int): Int {
        require(count >= 0) {
            "Count must be non-negative, but was $count"
        }

        if (count == 0) {
            return getCount(element = element)
        }

        val mutableCount = _mutableCountByElement.getOrPut(element) {
            MutableCount()
        }

        val oldCount = mutableCount.get()

        mutableCount.increase(delta = count)

        _size += count

        return oldCount
    }

    override fun remove(element: E): Boolean {
        val mutableCount = _mutableCountByElement[element] ?: return false

        mutableCount.decrease()

        if (mutableCount.get() == 0) {
            _mutableCountByElement.remove(element)
        }

        _size -= 1

        return true
    }

    override fun getCount(element: E): Int = when (val mutableCount = _mutableCountByElement[element]) {
        null -> 0
        else -> mutableCount.get()
    }
}
