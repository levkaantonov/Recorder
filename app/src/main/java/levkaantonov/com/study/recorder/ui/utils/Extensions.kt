package levkaantonov.com.study.recorder.ui.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import levkaantonov.com.study.recorder.R
import levkaantonov.com.study.recorder.delegates.ArgsDelegate
import levkaantonov.com.study.recorder.delegates.LazyProvider
import levkaantonov.com.study.recorder.delegates.ViewBindingFragmentDelegate


fun Activity.isServiceRunning(): Boolean {
    val manager: ActivityManager =
        getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    manager.getRunningServices(Int.MAX_VALUE).forEach {
        if (getString(R.string.recordServiceName) == it.service.className)
            return true
    }
    return false
}

fun <T : ViewBinding> Fragment.viewBinding(inflaterFactory: (inflater: LayoutInflater) -> T) =
    ViewBindingFragmentDelegate(this, inflaterFactory)


inline fun <reified T> Fragment.args(): LazyProvider<Fragment, T> =
    ArgsDelegate {
        it.arguments ?: throw RuntimeException("No args passed")
    }

