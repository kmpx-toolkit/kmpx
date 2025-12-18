package dev.kmpx.js

/**
 * A [FinalizationRegistry] object lets you request a callback when a value is garbage-collected.
 */
external class FinalizationRegistry<HeldValueT, UnregisterTokenT>(
    cleanupCallback: (heldValue: HeldValueT) -> Unit,
) {
    /**
     * Registers a value with this FinalizationRegistry so that if the value is garbage-collected, the registry's callback may get called.
     */
    fun register(
        target: dynamic,
        heldValue: HeldValueT,
        unregisterToken: UnregisterTokenT = definedExternally,
    )

    /**
     * @return A boolean value that is true if at least one cell was unregistered and false if no cell was unregistered.
     */
    fun unregister(
        unregisterToken: UnregisterTokenT,
    ): Boolean
}
