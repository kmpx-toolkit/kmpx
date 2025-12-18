package dev.kmpx.platform

import java.lang.ref.WeakReference

actual class PlatformWeakReference<T : Any> actual constructor(value: T) {
    private val weakReference = WeakReference(value)

    actual fun get(): T? = weakReference.get()
}
