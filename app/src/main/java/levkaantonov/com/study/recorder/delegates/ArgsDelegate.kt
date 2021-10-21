package levkaantonov.com.study.recorder.delegates

import android.os.Bundle
import kotlin.reflect.KProperty

class ArgsDelegate<F, T> constructor(
    private val bundleFactory: (F) -> Bundle
) : LazyProvider<F, T> {

    override fun provideDelegate(thisRef: F, property: KProperty<*>) =
        lazy {
            val bundle = bundleFactory(thisRef)
            bundle.get(property.name) as T
        }
}

interface LazyProvider<A, T> {
    operator fun provideDelegate(thisRef: A, property: KProperty<*>): Lazy<T>
}