package levkaantonov.com.study.recorder.delegates

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewBindingFragmentDelegate<T : ViewBinding>(
    private val fragment: Fragment,
    private val inflaterFactory: (inflater: LayoutInflater) -> T
) : ReadOnlyProperty<Fragment, T>, LifecycleObserver {

    private var binding: T? = null
    private val viewLifecycleObserver = object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        private fun onDestroy() {
            binding = null
            fragment.viewLifecycleOwner.lifecycle.removeObserver(this)
        }
    }
    private val viewLifecycleOwnerObserver = Observer<LifecycleOwner?> { viewLifecycleOwner ->
        viewLifecycleOwner ?: return@Observer
        viewLifecycleOwner.lifecycle.addObserver(viewLifecycleObserver)
    }

    init {
        fragment.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        fragment.viewLifecycleOwnerLiveData.observeForever(viewLifecycleOwnerObserver)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        fragment.viewLifecycleOwnerLiveData.removeObserver(viewLifecycleOwnerObserver)
        fragment.lifecycle.removeObserver(this)
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (binding != null) {
            return binding!!
        }
        val state = fragment.viewLifecycleOwner.lifecycle.currentState
        if (!state.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException(
                "Should not attempt to get bindings when Fragment views are destroyed."
            )
        }
        binding = inflaterFactory(thisRef.layoutInflater)
        return binding as T
    }
}