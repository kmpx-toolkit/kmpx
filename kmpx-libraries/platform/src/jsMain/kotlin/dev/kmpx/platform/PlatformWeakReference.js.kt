package dev.kmpx.platform

import dev.kmpx.js.WeakRef

actual class PlatformWeakReference<T : Any> actual constructor(value: T) {
    private val weakRef = WeakRef(value)

    actual fun get(): T? = weakRef.deref()
}
