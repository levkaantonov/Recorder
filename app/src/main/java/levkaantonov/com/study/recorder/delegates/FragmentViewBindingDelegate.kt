package levkaantonov.com.study.recorder.delegates

import android.view.LayoutInflater
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingDelegate<F, T : ViewBinding>(
    private val inflaterFactory: (inflater: LayoutInflater) -> T
) : ReadOnlyProperty<F, T>, LifecycleObserver {

    private var binding: T? = null
    private var fragment: Fragment? = null
    private val viewLifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        private fun onDestroy() {
            binding = null
            fragment?.let { it.viewLifecycleOwner.lifecycle.removeObserver(this) }
            fragment = null
        }
    }
    private val viewLifecycleOwnerObserver = Observer<LifecycleOwner?> { viewLifecycleOwner ->
        viewLifecycleOwner ?: return@Observer
        viewLifecycleOwner.lifecycle.addObserver(viewLifecycleObserver)
    }

    init {
        fragment?.let { it.lifecycle.addObserver(this) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        fragment?.let { it.viewLifecycleOwnerLiveData.observeForever(viewLifecycleOwnerObserver) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        fragment?.let {
            it.viewLifecycleOwnerLiveData.removeObserver(viewLifecycleOwnerObserver)
            it.lifecycle.removeObserver(this)
        }
    }

    @MainThread
    override fun getValue(thisRef: F, property: KProperty<*>): T {
        if (thisRef !is Fragment) {
            throw IllegalStateException(
                "F is not type of Fragment."
            )
        }

        this.fragment = thisRef
        if (binding != null) {
            return binding!!
        }
        fragment?.let {
            val state = it.viewLifecycleOwner.lifecycle.currentState
            if (!state.isAtLeast(Lifecycle.State.INITIALIZED)) {
                throw IllegalStateException(
                    "Should not attempt to get bindings when Fragment views are destroyed."
                )
            }
        }
        binding = inflaterFactory(thisRef.layoutInflater)
        return binding as T
    }
}