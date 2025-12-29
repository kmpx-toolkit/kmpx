package dev.kmpx.collections.internal.data_structures.linked_path.test_utils

data class WithNeighbours<T, R>(
    val prevElement: R,
    val element: T,
    val nextElement: R,
) where T : R

fun <T, R> Sequence<T>.withNeighbours(
    buildOuterLeft: (T) -> R,
    buildOuterRight: (T) -> R,
): Sequence<WithNeighbours<T, R>> where T : R = sequence {
    val iterator = iterator()
    if (!iterator.hasNext()) return@sequence

    var current = iterator.next()
    var prev: R = buildOuterLeft(current)

    while (iterator.hasNext()) {
        val next = iterator.next()
        yield(WithNeighbours(prev, current, next))
        prev = current
        current = next
    }

    yield(WithNeighbours(prev, current, buildOuterRight(current)))
}

fun <T : Any> Sequence<T>.withNeighboursOrNull(): Sequence<WithNeighbours<T, T?>> = this.withNeighbours(
    outerLeft = null,
    outerRight = null,
)

fun <T, R> Sequence<T>.withNeighbours(
    outerLeft: R,
    outerRight: R,
): Sequence<WithNeighbours<T, R>> where T : R = this.withNeighbours(
    buildOuterLeft = { outerLeft },
    buildOuterRight = { outerRight },
)

fun <T> Sequence<T>.withNeighboursSaturated(): Sequence<WithNeighbours<T, T>> = this.withNeighbours(
    buildOuterLeft = { it },
    buildOuterRight = { it },
)

fun <T, R> Iterable<T>.withNeighbours(
    outerLeft: R,
    outerRight: R,
): List<WithNeighbours<T, R>> where T : R = this.asSequence().withNeighbours(
    outerLeft = outerLeft,
    outerRight = outerRight,
).toList()
