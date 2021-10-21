package levkaantonov.com.study.recorder.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import levkaantonov.com.study.recorder.ui.utils.viewBinding

typealias InflateFactory<T> = (inflater: LayoutInflater) -> T

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    abstract val inflateFactory: InflateFactory<VB>
    private val binding: VB by viewBinding(inflateFactory)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}
abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment() {

    abstract val inflateFactory: InflateFactory<VB>
    internal val binding: VB by viewBinding(inflateFactory)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}