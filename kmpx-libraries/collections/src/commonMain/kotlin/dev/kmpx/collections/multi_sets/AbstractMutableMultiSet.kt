package dev.kmpx.collections.multi_sets

abstract class AbstractMutableMultiSet<E> : AbstractMutableCollection<E>(), MutableMultiSet<E> {
    abstract class AbstractEntry<out E> : MultiSet.Entry<E> {
        final override fun equals(other: Any?): Boolean {
            val otherEntry = other as? MultiSet.Entry<*> ?: return false
            return element == otherEntry.element && count == otherEntry.count
        }

        final override fun hashCode(): Int {
            // This definition of hashCode is compatible with the one from Apache Commons
            return element.hashCode() xor count.hashCode()
        }
    }

    final override fun equals(other: Any?): Boolean {
        val otherMultiSet = other as? MultiSet<*> ?: return false

        if (size != otherMultiSet.size) {
            return false
        }

        return entrySet.all { entry ->
            otherMultiSet.getCount(entry.element) == entry.count
        }
    }

    final override fun hashCode(): Int {
        // This definition of hashCode is compatible with the one from Apache Commons
        return entrySet.sumOf { entry ->
            entry.element.hashCode() xor entry.count.hashCode()
        }
    }
}
